package cua;

import java.lang.RuntimeException;

import java.util.*;

import soot.*;
import soot.jimple.FieldRef;
import soot.jimple.internal.*;

public class AnalysisTransformer extends SceneTransformer
{
	private Map<String, SootField> suspectContainers = new HashMap<>();

	private Map<SootClass, SootField> classToContainer = new HashMap<>();
	private Map<SootClass, Set<SootMethod>> classToReferredMethods = new HashMap<>();
	private Map<SootClass, Set<SootField>> classToNotUsedField = new HashMap<>();

	@Override
	protected void internalTransform(String phaseName, Map options)
	{
        // get all the java.util.* containers
		for (SootClass clazz : Scene.v().getApplicationClasses()) {
            for (SootField field : clazz.getFields()) {
                if (field.getType().toString().startsWith("java.util.") && !field.isPublic()) {
                    this.suspectContainers.put(field.getSignature(), field);
                    System.out.println("[cua] Container to analysis: " + field.getDeclaration());
                }
            }
        }

        // go through all the methods
        for (SootClass clazz : Scene.v().getApplicationClasses()) {
			for (SootMethod method : clazz.getMethods()) {
				try {
                    Body body = method.getActiveBody();
                    System.out.println("[cua] In method: " + clazz.getName()
                            + " " + method.getSignature());

                    Map<Value, String> referredContainer = new HashMap<>();
                    Map<Value, Value> referredElements = new HashMap<>();
                    Map<Value, Set<SootMethod>> referredMethods = new HashMap<>();

                    for (Unit unit : body.getUnits()) {
                        if (unit instanceof JAssignStmt) {
                            JAssignStmt assignStmt = (JAssignStmt) unit;

                            // record all referred suspect containers in this method
                            try {
                                String fieldSignature = assignStmt.getFieldRef().getField().getSignature();
                                if (this.suspectContainers.containsKey(fieldSignature)) {
                                    referredContainer.put(assignStmt.getLeftOp(), fieldSignature);
                                    System.out.println(
                                            "[cua] Container " + fieldSignature
                                                    + " referenced as JimpleLocal " + assignStmt.getLeftOp());
                                }
                            } catch (RuntimeException e) {
                                // Ignore
                                // java.lang.RuntimeException: getFieldRef() called with no FieldRef present!
                            }

                            // record the element that is got from one of referredContainer
                            try {
                                JInterfaceInvokeExpr expr = (JInterfaceInvokeExpr) assignStmt.getInvokeExpr();
                                JimpleLocal base = (JimpleLocal) expr.getBase();
                                JimpleLocal element = (JimpleLocal) assignStmt.getLeftOp();
                                if (referredContainer.containsKey(base) && expr.getMethod().getName().equals("get")) {
                                    referredElements.put(element, base);
                                    System.out.println(
                                            "[cua] Element get from container " + referredContainer.get(base)
                                                    + " as JimpleLocal " + element);
                                }
                            } catch (RuntimeException e) {
                                // Ignore
                                // java.lang.RuntimeException: getInvokeExpr() called with no InvokeExpr present!
                            }

                            // record the method that a referredElement is called on
                            try {
                                JVirtualInvokeExpr expr = (JVirtualInvokeExpr) assignStmt.getInvokeExpr();
                                JimpleLocal element = (JimpleLocal) expr.getBase();
                                SootMethod calledMethod = expr.getMethod();
                                if (referredElements.containsKey(element)) {
                                    SootClass elementClass = calledMethod.getDeclaringClass();
                                    if (this.classToContainer.get(elementClass) == null) {
                                        JimpleLocal container = (JimpleLocal) referredElements.get(element);
                                        String containerSignature = referredContainer.get(container);
                                        this.classToContainer.put(elementClass, suspectContainers.get(containerSignature));
                                        this.classToReferredMethods.put(elementClass, new HashSet<>());
                                    }
                                    this.classToReferredMethods.get(elementClass).add(calledMethod);

                                    System.out.println(
                                            "[cua] Method " + calledMethod.getSignature()
                                                    + " called on element " + element);
                                }
                            } catch (RuntimeException e) {
                                // Ignore
                                // java.lang.RuntimeException: getInvokeExpr() called with no InvokeExpr present!
                            }
                        }
                    }
				} catch (RuntimeException e) {
				    // Ignore
				    // java.lang.RuntimeException: no active body present for method <void <init>()>
				}
			}
		}

		// check if all fields is referred
        for (SootClass clazz : this.classToReferredMethods.keySet()) {
            this.classToNotUsedField.put(clazz, new HashSet<>());
            this.classToNotUsedField.get(clazz).addAll(clazz.getFields());

            // remove fields referred in called methods
            for (SootMethod method : this.classToReferredMethods.get(clazz)) {
                try {
                    Body body = method.getActiveBody();

                    for (Unit unit : body.getUnits()) {
                        if (unit instanceof JAssignStmt) {
                            JAssignStmt assignStmt = (JAssignStmt) unit;
                            try {
                                FieldRef fieldRef = assignStmt.getFieldRef();
                                this.classToNotUsedField.get(clazz).remove(fieldRef.getField());
                            } catch (RuntimeException e) {
                                // Ignore
                                // java.lang.RuntimeException: getFieldRef() called with no FieldRef present!
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    // Ignore
                    // java.lang.RuntimeException: no active body present for method <void <init>()>
                }
            }
        }

        // container usage analysis report
        for (SootClass clazz : this.classToNotUsedField.keySet()) {
		    if (!this.classToNotUsedField.get(clazz).isEmpty()) {

                StringBuilder buf = new StringBuilder();
		        for (SootField field : this.classToNotUsedField.get(clazz)) {
                    buf.append(field.getName());
                    buf.append(" ");
                }

		        System.out.println(
		                "[cua-report] " + clazz.getName()
                        + " is added to container " + this.classToContainer.get(clazz).getSignature()
                        + " but fields " + buf.toString() + "not used in container"
                );
            }
        }
	}
}
