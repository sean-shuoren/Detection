package mock;

import java.util.Map;
import java.util.HashMap;


public class MockContextConfig {

    protected final Map<String, JavaClassCacheEntry> javaClassCache = new HashMap<String, JavaClassCacheEntry>();

    private static class JavaClassCacheEntry {
        private final MockJavaClass javaClass;
        
        public JavaClassCacheEntry(MockJavaClass javaClass) {
            this.javaClass = javaClass;
        }

        public MockJavaClass getJavaClass() {
            return javaClass;
        }
    }

    // Mock of the process of finding Class from the context
    public void populateJavaClass(String className) {
        MockJavaClass javaClass = new MockJavaClass(className);
        this.javaClassCache.put(javaClass.getClassName(), new JavaClassCacheEntry(javaClass));
    }

    public void populateRelatedJavaClass(String className) {
        if (!javaClassCache.containsKey(className)) {
            return;
        }

        String superClassName = javaClassCache.get(className).getJavaClass().getSuperclassName();
        this.populateJavaClass(superClassName);
        
        for (String iterfaceName : javaClassCache.get(className).getJavaClass().getInterfaceNames()) {
            this.populateJavaClass(iterfaceName);
        }
    }    

    public void logCache() {
        System.out.println("MockContextConfig.javaClassCache:");
        for (JavaClassCacheEntry e : this.javaClassCache.values()) {
            System.out.println(e.getJavaClass());
        }
    }

}