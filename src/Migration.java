import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Migration {

    private static final String PATH_TO_JAR =
            "file:/home/bresiu/Sonda/Prevayler/out/artifacts/Prevayler_jar/Prevayler.jar";

    private static final String PATH_TO_JOURNALS = "/home/bresiu/Sonda/Prevayler/Prevayler Demo";

    public static void go() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        // 1. make List of URLs to pass to CustomClassLoader class
        URL url = new URL(PATH_TO_JAR);
        List<URL> urls = new ArrayList<URL>();
        urls.add(url);

        // 2. Use CustomLoaderClass, to make sure, that loaded classes/methods are not from current project,
        // but from the jar specified in URL, since Java class loaders (including URLClassLoader)
        // first ask to load classes from their parent class loader.
        CustomClassLoader clsLoader = new CustomClassLoader(urls);
        java.lang.Class cls = clsLoader.loadClass("GetNumbers");

        // String.class in methods second parametr means, that we should pass String to that method
        Method method = cls.getMethod("migrate", String.class);

        method.invoke(null, PATH_TO_JOURNALS);

        // 3. invoke method
        List numbers = (List) method.invoke(null, PATH_TO_JOURNALS);

        System.out.println(numbers.size());
        System.out.println(numbers);

        java.lang.Class clsNumbers = clsLoader.loadClass("Numbers");
        Method methodInt = clsNumbers.getMethod("getIntVariable");

        Integer test = (Integer) methodInt.invoke(null);

        System.out.println("Variable: " + test);

        ////////////////////////////////////

        java.lang.Class numbersClass = clsLoader.loadClass("Numbers"); // Class "Numbers"
        Method getCast = numbersClass.getMethod("getCast"); // Method "getCast"
        java.lang.Class castClass = clsLoader.loadClass("Cast"); // Class "Cast"
        Object cast = getCast.invoke(null); // Object of type "Cast"
        System.out.println(castClass.getMethod("getCastString").invoke(cast));
    }
}