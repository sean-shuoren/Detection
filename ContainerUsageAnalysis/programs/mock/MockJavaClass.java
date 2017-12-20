package mock;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MockJavaClass {
    private String file_name;
    private String source_file_name;
    private String class_name;
    private String superclass_name;
    private int major, minor; // Compiler version
    private String[] interface_names;

    public MockJavaClass(String className) {
        this.file_name = "<file-" + className + ">";
        this.source_file_name = "<source_file-" + className + ">";
        this.major = 5;
        this.minor = 82;
        this.superclass_name = "<MockSuperClass-" + className + ">";
        this.class_name = className;
        this.interface_names = new String[3];
        for (int i = 0; i < interface_names.length; i++) {
            interface_names[i] = "<MockInterface-" + className + "-" + Integer.toString(i) + ">";
        }
    }

    public String getClassName() {
        return class_name;
    }

    public String[] getInterfaceNames() {
        return interface_names;
    }

    public String getSuperclassName() {
        return superclass_name;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(128);
        buf.append(class_name).append('\n');
        buf.append("extends\t\t\t").append(superclass_name).append('\n');
        int size = interface_names.length;
        if (size > 0) {
            buf.append("implements\t\t");
            for (int i = 0; i < size; i++) {
                buf.append(interface_names[i]);
                if (i < size - 1) {
                    buf.append(", ");
                }
            }
            buf.append('\n');
        }
        buf.append("filename\t\t").append(file_name).append('\n');
        buf.append("compiled from\t\t").append(source_file_name).append('\n');
        buf.append("compiler version\t").append(major).append(".").append(minor).append('\n');
        return buf.toString();
    }
}
