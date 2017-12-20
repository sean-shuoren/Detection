package mock;

public class TestMock
{
	public static void main(String[] args)
	{
		MockContextConfig context = new MockContextConfig();
		String className = "sample";
		context.populateJavaClass(className);
		context.populateRelatedJavaClass(className);
		context.logCache();
	}
}
