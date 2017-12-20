package cua;

import soot.PackManager;
import soot.Transform;

public class RunContainerUsageAnalysis
{
	public static void main(String[] args)
    {
        String classPath = System.getProperty("user.dir") + "/programs";

        // for some reason, when input is java source code
        // it throws Exception: Error: Failed to load java.util.Map$Entry.
        String[] sootArgs = {
                "-cp", classPath, "-pp",
                "-w",
                "-src-prec", "class",
                "-f", "class",
                "mock.TestMock", "mock.MockContextConfig", "mock.MockJavaClass"     // add your class names here
        };

        // Create transformer for analysis
		AnalysisTransformer transformer = new AnalysisTransformer();

		// Add transformer to appropriate Pack in PackManager. PackManager will run all Packs when main function of Soot is called
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.cua", transformer));

        soot.Main.main(sootArgs);
	}
}
