/**
 * This class assists in getting the methods to a reflection object and also comparing a reflection object to a
 * likely past incarnation of the object.
 * This will not compare any objects that are arrays or contain parameters.
 * <p>
 * Examples of usage
 * <p>
 if (lastOutputs.isEmpty()) {
 lastOutputs = OutputObjectComparer.getListOfMethodOutputsEx(player, new Integer[]{},
 new String[]{}, new String[]{"boolean", "void"}, new String[]{"get", "isFriend", "isClanMember"}, new Integer[]{0},
 Parameters.TYPE, Parameters.NAME, Parameters.OUTPUT);
 } else {

 test = new OutputObjectComparer(player, new Integer[]{}, new String[]{}, new String[]{"boolean", "void"},
 new String[]{"get", "isFriend", "isClanMember"}, new Integer[]{0});
 String namesOfNonmatchingMethods = OutputObjectComparer.convertToStringArrayStyleEx
 (test.checkForMatchingMethodOutputsEx(lastOutputs, Parameters.TYPE, Parameters.NAME, Parameters.OUTPUT));

 lastOutputs = OutputObjectComparer.getListOfMethodOutputsEx(player, new Integer[]{},
 new String[]{"int"}, new String[]{"boolean", "void"}, new String[]{"get", "isFriend", "isClanMember"}, new Integer[]{0},
 Parameters.TYPE, Parameters.NAME, Parameters.OUTPUT);

 log.debug(test.getMethodOuputsEx(Parameters.TYPE, Parameters.NAME, Parameters.OUTPUT));

 log.debug("\n" + namesOfNonmatchingMethods);
 }
 *
 * @author Gigia
 */
package rsb.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class OutputObjectComparer {
    private String[] stringsToIgnoreInName;
    private String[] typesToIgnore;
    private String[] typesToInclude;
    private Integer[] modifiersToInclude;
    private Integer[] parameterConstraints;
    private Object object;

    /**
     * Creates an outputobjectcomparer with the given parameters
     *
     * @param object                the object to compare methods of
     * @param parameterConstraints  the numbers of parameters to look for (usually going to just hold zero)
     * @param modifiersToInclude    the modifiers of the methods to include when checking the methods
     * @param returnTypesToInclude  the return types of the methods to include when checking the methods
     * @param returnTypesToIgnore   the return types of the methods to ignore when checking the methods
     * @param stringsToIgnoreInName the strings in the names of methods to ignore when checking the methods
     */
    public OutputObjectComparer(Object object, Integer[] modifiersToInclude, String[] returnTypesToInclude, String[] returnTypesToIgnore, String[] stringsToIgnoreInName, Integer[] parameterConstraints) {
        this.object = object;
        this.parameterConstraints = parameterConstraints;
        this.modifiersToInclude = modifiersToInclude;
        this.typesToInclude = returnTypesToInclude;
        this.typesToIgnore = returnTypesToIgnore;
        this.stringsToIgnoreInName = stringsToIgnoreInName;
    }

    /**
     * Gets the output list based on this objects variables and parameters allowing one to add extra
     * information to the key. Additionally one may pass a method that performs a conditional statement
     * to allow further sorting of the array.
     * <p>
     * IE one should make a method in the class they are calling this from then use reflection
     * and get the method from that class like so: exampleClass.class.getMethod("conditional1", int.class)
     *
     * @param param parameter array to decide what to values to return in the key
     * @param conditionals the conditional values to use to impact the output
     * @return the list of keys and outputs of the object's methods respective to the parameters
     */
    public HashMap<String, Object> getListOfMethodOutputsEx(Parameters[] param, Method... conditionals) {
        ArrayList<String> ignoredReturnTypes = new ArrayList<>(Arrays.asList(typesToIgnore));
        ArrayList<Integer> includedModifiers = new ArrayList<>(Arrays.asList(modifiersToInclude));
        ArrayList<Integer> parameterCounts = new ArrayList<>(Arrays.asList(parameterConstraints));
        ArrayList<String> includedReturnTypes = new ArrayList<>(Arrays.asList(typesToInclude));
        ArrayList<String> ignoredNames = new ArrayList<>(Arrays.asList(stringsToIgnoreInName));
        HashMap<String, Object> outputs = new HashMap<>();
        Method[] methods;
        if (object != null) {
            //Checks the parameters to decide whether we wanted the declared methods or the regular ones
            //If not specified it simply uses the regular fields
            methods = (param.length > 0 && (Arrays.asList(param).contains(Parameters.DECLARED))) ? object.getClass().getDeclaredMethods() : object.getClass().getMethods();
        }
        else {
            return null;
        }
        for (Method i : methods) {
            i.setAccessible(true);
            if (parameterCounts.isEmpty() || parameterCounts.contains(i.getParameterCount())) {
                if (includedModifiers.isEmpty() || includedModifiers.contains(i.getModifiers())) {
                    if ((ignoredReturnTypes.isEmpty() && includedReturnTypes.isEmpty())
                            || (!ignoredReturnTypes.contains(i.getReturnType().getName())) &&
                            (includedReturnTypes.isEmpty() || includedReturnTypes.contains(i.getReturnType().getName()))) {
                        if (ignoredNames.isEmpty() || ignoredNames.stream().noneMatch(t -> i.getName().contains(t))) {
                            Object output = null;
                            String outputString = "";
                            for (int x = 0; x < param.length; x++) {
                                switch (param[x]) {
                                    case PARAMETER_COUNT:
                                        outputString += i.getParameterCount();
                                        if (x < param.length - 1)
                                            outputString += " : ";
                                        break;
                                    case TYPE:
                                        outputString += i.getReturnType().getName();
                                        if (x < param.length - 1)
                                            outputString += " : ";
                                        break;
                                    case MODIFIERS:
                                        outputString += i.getModifiers();
                                        if (x < param.length - 1)
                                            outputString += " : ";
                                        break;
                                    case NAME:
                                        outputString += i.getName();
                                        if (x < param.length - 1)
                                            outputString += " : ";
                                        break;
                                    case OUTPUT:
                                        try {
                                            output = i.invoke(object);
                                        } catch (Exception e) {
                                        }
                                        break;
                                    case DECLARED:
                                        //This is just a modifier to decide what returns to have so we do nothing
                                        break;
                                }
                            }
                            boolean isConditionals = false;
                            if (conditionals.length > 0) {
                                for (Method method : conditionals) {
                                    try {
                                        if ((boolean) method.invoke(method, output)) {
                                            isConditionals = true;
                                        } else {
                                            isConditionals = false;
                                            break;
                                        }
                                    } catch (Exception e) {
                                        isConditionals = false;
                                        break;
                                    }
                                }
                            } else {
                                isConditionals = true;
                            }
                            if (isConditionals)
                                outputs.put(outputString, output);
                        }
                    }
                }
            }
        }
        return outputs;
    }

    /**
     * Gets the output list based on this objects variables and parameters allowing one to add extra
     * information to the key. Additionally one may pass a method that performs a conditional statement
     * to allow further sorting of the array.
     * <p>
     * IE one should make a method in the class they are calling this from then use reflection
     * and get the method from that class like so: exampleClass.class.getMethod("conditional1", int.class)
     *
     * @param param parameter array to decide what to values to return in the key
     * @param conditionals the conditional values to use to impact the output
     * @return the list of keys and outputs of the object's methods respective to the parameters
     */
    public HashMap<String, Object> getListOfFieldOutputsEx(Parameters[] param, Method... conditionals) {
        ArrayList<String> ignoredTypes = new ArrayList<>(Arrays.asList(typesToIgnore));
        ArrayList<Integer> includedModifiers = new ArrayList<>(Arrays.asList(modifiersToInclude));
        ArrayList<String> includedTypes = new ArrayList<>(Arrays.asList(typesToInclude));
        ArrayList<String> ignoredNames = new ArrayList<>(Arrays.asList(stringsToIgnoreInName));
        HashMap<String, Object> outputs = new HashMap<>();

        Field[] fields;
        if (object != null) {
            //Checks the parameters to decide whether we wanted the declared fields or the regular ones
            //If not specified it simply uses the regular fields
            fields = (param.length > 0 && (Arrays.asList(param).contains(Parameters.DECLARED))) ? object.getClass().getDeclaredFields() : object.getClass().getFields();
        }
        else {
            return null;
        }

        for (Field i : fields) {
            i.setAccessible(true);
            if (includedModifiers.isEmpty() || includedModifiers.contains(i.getModifiers())) {
                if ((ignoredTypes.isEmpty() && includedTypes.isEmpty())
                        || (!ignoredTypes.contains(i.getType().getName())) &&
                        (includedTypes.isEmpty() || includedTypes.contains(i.getType().getName()))) {
                    if (ignoredNames.isEmpty() || ignoredNames.stream().noneMatch(t -> i.getName().contains(t))) {
                        Object output = null;
                        String outputString = "";
                        for (int x = 0; x < param.length; x++) {
                            switch (param[x]) {
                                case TYPE:
                                    outputString += i.getType().getName();
                                    if (x < param.length - 1)
                                        outputString += " : ";
                                    break;
                                case MODIFIERS:
                                    outputString += i.getModifiers();
                                    if (x < param.length - 1)
                                        outputString += " : ";
                                    break;
                                case NAME:
                                    outputString += i.getName();
                                    if (x < param.length - 1)
                                        outputString += " : ";
                                    break;
                                case OUTPUT:
                                    try {
                                        output = i.get(object);
                                    } catch (Exception e) {
                                    }
                                    break;
                                case DECLARED:
                                    //This is just a modifier to decide what returns to have so we do nothing
                                    break;
                            }
                        }
                        boolean isConditionals = false;
                        if (conditionals.length > 0) {
                            for (Method method : conditionals) {
                                try {
                                    if ((boolean) method.invoke(method, output)) {
                                        isConditionals = true;
                                    } else {
                                        isConditionals = false;
                                        break;
                                    }
                                } catch (Exception e) {
                                    isConditionals = false;
                                    break;
                                }
                            }
                        } else {
                            isConditionals = true;
                        }
                        if (isConditionals)
                            outputs.put(outputString, output);
                    }
                }

            }
        }
        return outputs;
    }

    /**
     * Returns a hashmap of given parameters for the keys
     * and objects of any method for the values and also respective to the parameters supplied
     *
     * @param objectToInvoke       the object to check
     * @param parameterConstraints an array of parameter counts to allow
     * @param modifiersToInclude   the modifiers of the method to include IE public static final ect
     * @param returnTypesToInclude the return types to include
     * @param returnTypesToIgnore  the return types to ignore
     * @param namesToIgnore        the names/strings in the names to ignore
     * @param param                parameter array to decide what to values to return in the key
     * @return the list of names and outputs of the object's methods respective to the parameters
     */
    public static HashMap<String, Object> getListOfMethodOutputsEx(Object objectToInvoke, Integer[] modifiersToInclude,
                                                                   String[] returnTypesToInclude, String[] returnTypesToIgnore,
                                                                   String[] namesToIgnore, Integer[] parameterConstraints, Parameters... param) {
        ArrayList<String> ignoredReturnTypes = new ArrayList<>(Arrays.asList(returnTypesToIgnore));
        ArrayList<Integer> includedModifiers = new ArrayList<>(Arrays.asList(modifiersToInclude));
        ArrayList<Integer> parameterCounts = new ArrayList<>(Arrays.asList(parameterConstraints));
        ArrayList<String> includedReturnTypes = new ArrayList<>(Arrays.asList(returnTypesToInclude));
        ArrayList<String> ignoredNames = new ArrayList<>(Arrays.asList(namesToIgnore));
        HashMap<String, Object> outputs = new HashMap<>();

        Method[] methods;
        if (objectToInvoke != null) {
            //Checks the parameters to decide whether we wanted the declared methods or the regular ones
            //If not specified it simply uses the regular fields
            methods = (param.length > 0 && (Arrays.asList(param).contains(Parameters.DECLARED))) ? objectToInvoke.getClass().getDeclaredMethods() : objectToInvoke.getClass().getMethods();
        }
        else {
            return null;
        }
        for (Method i : methods) {
            i.setAccessible(true);
            if (parameterCounts.isEmpty() || parameterCounts.contains(i.getParameterCount())) {
                if (includedModifiers.isEmpty() || includedModifiers.contains(i.getModifiers())) {
                    if ((ignoredReturnTypes.isEmpty() && includedReturnTypes.isEmpty())
                            || (!ignoredReturnTypes.contains(i.getReturnType().getName())) &&
                            (includedReturnTypes.isEmpty() || includedReturnTypes.contains(i.getReturnType().getName()))) {
                        if (ignoredNames.isEmpty() || ignoredNames.stream().noneMatch(t -> i.getName().contains(t))) {
                            Object output = null;
                            try {
                                output = i.invoke(objectToInvoke);
                            } catch (Exception e) {
                            }
                            String outputString = "";
                            for (int x = 0; x < param.length; x++) {
                                switch (param[x]) {
                                    case PARAMETER_COUNT:
                                        outputString += i.getParameterCount();
                                        if (x < param.length - 1)
                                            outputString += " : ";
                                        break;
                                    case TYPE:
                                        outputString += i.getReturnType().getName();
                                        if (x < param.length - 1)
                                            outputString += " : ";
                                        break;
                                    case MODIFIERS:
                                        outputString += i.getModifiers();
                                        if (x < param.length - 1)
                                            outputString += " : ";
                                        break;
                                    case NAME:
                                        outputString += i.getName();
                                        if (x < param.length - 1)
                                            outputString += " : ";
                                        break;
                                    case OUTPUT:
                                        try {
                                            output = i.invoke(objectToInvoke);
                                        } catch (Exception e) {
                                        }
                                        break;
                                    case DECLARED:
                                        //This is just a modifier to decide what returns to have so we do nothing
                                        break;
                                }
                            }
                            outputs.put(outputString, output);

                        }
                    }
                }
            }
        }
        return outputs;
    }

    /**
     * Returns a hashmap of names and objects of any method with no parameters and respective to the parameters supplied
     *
     * @return the list of names and outputs of the object's methods respective to the parameters
     */
    public HashMap<String, Object> getListOfMethodOutputs() {
        ArrayList<String> ignoredReturnTypes = new ArrayList<>(Arrays.asList(typesToIgnore));
        ArrayList<Integer> includedModifiers = new ArrayList<>(Arrays.asList(modifiersToInclude));
        ArrayList<Integer> parameterCounts = new ArrayList<>(Arrays.asList(parameterConstraints));
        ArrayList<String> includedReturnTypes = new ArrayList<>(Arrays.asList(typesToInclude));
        ArrayList<String> ignoredNames = new ArrayList<>(Arrays.asList(stringsToIgnoreInName));
        HashMap<String, Object> outputs = new HashMap<>();
        for (Method i : object.getClass().getMethods()) {
            i.setAccessible(true);
            if (parameterCounts.isEmpty() || parameterCounts.contains(i.getParameterCount())) {
                if (includedModifiers.isEmpty() || includedModifiers.contains(i.getModifiers())) {
                    if ((ignoredReturnTypes.isEmpty() && includedReturnTypes.isEmpty())
                            || (!ignoredReturnTypes.contains(i.getReturnType().getName())) &&
                            (includedReturnTypes.isEmpty() || includedReturnTypes.contains(i.getReturnType().getName()))) {
                        if (ignoredNames.isEmpty() || ignoredNames.stream().noneMatch(t -> i.getName().contains(t))) {
                            Object output = null;
                            try {
                                output = i.invoke(object);
                            } catch (Exception e) {
                            }
                            outputs.put(i.getName(), output);

                        }

                    }
                }

            }
        }

        return outputs;

    }

    /**
     * Returns a hashmap of names and objects of any method with no parameters and respective to the parameters supplied
     *
     * @param objectToInvoke       the object to check
     * @param classToCheck         the class of the object to invoke methods on
     * @param parameterConstraints an array of parameter counts to allow
     * @param modifiersToInclude   the modifiers of the method to include IE public static final ect
     * @param returnTypesToInclude the return types to include
     * @param returnTypesToIgnore  the return types to ignore
     * @param namesToIgnore        the names/strings in the names to ignore
     * @return the list of names and outputs of the object's methods respective to the parameters
     */
    public static HashMap<String, Object> getListOfMethodOutputs(Object objectToInvoke, Class<?> classToCheck, Integer[] modifiersToInclude, String[] returnTypesToInclude, String[] returnTypesToIgnore, String[] namesToIgnore, Integer[] parameterConstraints) {
        ArrayList<String> ignoredReturnTypes = new ArrayList<>(Arrays.asList(returnTypesToIgnore));
        ArrayList<Integer> includedModifiers = new ArrayList<>(Arrays.asList(modifiersToInclude));
        ArrayList<Integer> parameterCounts = new ArrayList<>(Arrays.asList(parameterConstraints));
        ArrayList<String> includedReturnTypes = new ArrayList<>(Arrays.asList(returnTypesToInclude));
        ArrayList<String> ignoredNames = new ArrayList<>(Arrays.asList(namesToIgnore));
        HashMap<String, Object> outputs = new HashMap<>();
        for (Method i : classToCheck.getMethods()) {
            i.setAccessible(true);
            if (parameterCounts.isEmpty() || parameterCounts.contains(i.getParameterCount())) {
                if (includedModifiers.isEmpty() || includedModifiers.contains(i.getModifiers())) {
                    if ((ignoredReturnTypes.isEmpty() && includedReturnTypes.isEmpty())
                            || (!ignoredReturnTypes.contains(i.getReturnType().getName())) &&
                            (includedReturnTypes.isEmpty() || includedReturnTypes.contains(i.getReturnType().getName()))) {
                        if (ignoredNames.isEmpty() || ignoredNames.stream().noneMatch(t -> i.getName().contains(t))) {
                            Object output = null;
                            try {
                                output = i.invoke(objectToInvoke);
                            } catch (Exception e) {
                            }
                            outputs.put(i.getName(), output);

                        }
                    }
                }

            }
        }

        return outputs;
    }


    /**
     * Creates a hashmap of strings and object outputs to then compares to another hashmap of strings and object outputs
     * to determine the differences and returns a list of the strings and objects that differ between the two
     *
     * @param compareTo The second hashmap to compare against for collisions
     *
     * @return the names of the outputs that did not match
     */
    public ArrayList<String> checkForMatching(HashMap<String, Object> compareTo) {
        ArrayList<String> notMatching = new ArrayList<>();
        String[] previousNames = compareTo.keySet().toArray(new String[compareTo.size()]);
        Object[] previousOutputs = compareTo.values().toArray();
        HashMap<String, Object> outputs = getListOfMethodOutputs();
        String[] currentNames = outputs.keySet().toArray(new String[compareTo.size()]);
        Object[] currentOutputs = outputs.values().toArray();
        for (int i = 0; i < compareTo.size(); i++) {
            if (previousOutputs[i] != currentOutputs[i]) {
                notMatching.add(previousNames[i]);
            }

        }
        return notMatching;
    }

    /**
     * Checks the first objects type and then compares it to the second object which should also be the same type
     *
     * @param firstObject  the first object to check
     * @param secondObject the object to compare to
     * @return false unless they match
     */
    public boolean checkForTypeAndNoMatch(Object firstObject, Object secondObject) {
        if (firstObject == null)
            if (secondObject != null)
                return true;
            else
                return false;

        if (firstObject instanceof Integer) {
            if ((int) firstObject != (int) secondObject) {
                return true;
            }
        }
        if (firstObject instanceof String) {
            if (!firstObject.equals(secondObject)) {
                return true;
            }
        }

        if (firstObject instanceof Long) {
            if ((long) firstObject != (long) secondObject) {
                return true;
            }
        }

        if (firstObject instanceof Double) {
            if ((double) firstObject != (double) secondObject) {
                return true;
            }
        }
        if (firstObject.getClass().isArray()) {
            Object[] firstArray;
            Object[] secondArray;
            try {
                firstArray = unpack(firstObject);
                secondArray = unpack(secondObject);
            } catch (NullPointerException e) {
                return true;
            }
            if (firstArray.length == secondArray.length) {
                for (int i = 0; i < firstArray.length; i++) {
                    if (checkForTypeAndNoMatch(firstArray[i], secondArray[i])) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a hashmap of strings and object outputs to then compares to another hashmap of strings and object outputs
     * to determine the differences and returns a hashmap of the strings and objects that differ between the two
     *
     * @param compareTo The second hashmap to compare against for collisions
     * @param param     The parameters to use as a filter on the second hashmap
     * @return the names of the outputs that did not match
     */
    public HashMap<String, Object> checkForMatchingMethodOutputsEx(HashMap<String, Object> compareTo, Parameters... param) {
        HashMap<String, Object> notMatching = new HashMap<>();
        String[] previousNames = (compareTo.size() > 0) ? compareTo.keySet().toArray(new String[compareTo.size()]) : new String[]{};
        Object[] previousOutputs = (compareTo.size() > 0) ? compareTo.values().toArray(new Object[compareTo.size()]) : new Object[]{};
        HashMap<String, Object> outputs = getListOfMethodOutputsEx(param);
        String[] currentNames = (outputs.size() > 0) ? outputs.keySet().toArray(new String[outputs.size()]) : new String[]{};
        Object[] currentOutputs = (outputs.size() > 0) ?  outputs.values().toArray(new Object[outputs.size()]) : new Object[]{};
        if (currentNames.length > 0 && previousNames.length > 0) {
            if (currentNames.length == previousNames.length) {
                for (int i = 0; i < compareTo.size(); i++) {
                    try {
                        if (checkForTypeAndNoMatch(previousOutputs[i], currentOutputs[i]))

                            notMatching.put(previousNames[i], previousOutputs[i]);

                    } catch
                    (NullPointerException e) {
                        System.err.println(e);
                    }
                }
                return notMatching;
            }
        }
        return null;
    }

    /**
     * Converts the given ArrayList containing strings to a string with the style of a String[]
     * eg String output = "{"item1", "item2", "item3", "item4"}"
     *
     * @param stringToConvert       ArrayList to convert to a string
     * @return                      A string comprised of the contents of the ArrayList
     */
    public static String convertToStringArrayStyle(ArrayList<?> stringToConvert) {
        String string = "Empty";
        if (!stringToConvert.isEmpty()) {
            string = "{";
            for (int i = 0; i < stringToConvert.size(); i++) {
                if (i != stringToConvert.size() - 1) {
                    string += "\"" + stringToConvert.get(i) + "\"" + ", ";
                }
            }
            string += "\"" + stringToConvert.get(stringToConvert.size() - 1) + "\"" + "}";
        }

        return string;
    }

    /**
     * Converts the given ArrayList containing strings to a string with the style of a String[]
     * eg String output = "{"item1", "item2", "item3", "item4"}"
     *
     * @param stringToConvert       List to convert to a string
     * @return                      A string comprised of the contents of the List
     */
    public static String convertToStringArrayStyle(List<?> stringToConvert) {
        String string = "Empty";
        if (!stringToConvert.isEmpty()) {
            string = "{";
            for (int i = 0; i < stringToConvert.size(); i++) {
                if (i != stringToConvert.size() - 1) {
                    string += "\"" + stringToConvert.get(i) + "\"" + ", ";
                }
            }
            string += "\"" + stringToConvert.get(stringToConvert.size() - 1) + "\"" + "}";
        }

        return string;
    }


    /**
     * Returns everything in the hashmap as a String[] styled string
     *
     * @param stringObjectHashMap the hashmap to convert
     * @param param     The parameters to use as a filter on the second hashmap
     * @return the string containing all the data in the hashmap as a string
     */
    public static String convertToStringArrayStyleEx(HashMap<String, Object> stringObjectHashMap, Parameters... param) {
        String string = "Empty";
        if (stringObjectHashMap != null) {
            List<String> strings = Arrays.asList(stringObjectHashMap
                    .keySet().toArray(new String[stringObjectHashMap.keySet().size()]));
            List<Object> objects = Arrays.asList(stringObjectHashMap
                    .values().toArray(new Object[stringObjectHashMap.values().size()]));
            if (!stringObjectHashMap.isEmpty()) {
                string = "{";
                for (int i = 0; i < stringObjectHashMap.size(); i++) {
                    if (i != stringObjectHashMap.size() - 1) {
                        string += "\"" + strings.get(i);
                        for (Parameters parameter : param)
                            if (parameter.equals(Parameters.OUTPUT))
                                string += objects.get(i);
                        string += "\"" + ", ";
                    }
                }
                string += "\"" + strings.get(strings.size() - 1);
                for (Parameters parameter : param)
                    if (parameter.equals(Parameters.OUTPUT))
                        string += objects.get(strings.size() - 1);
                string += "\"" + "}";
            }
        }
        return string;
    }

    /**
     * Returns a string of as much method outputs as the parameters passed for each object
     * as well as allowing the passing of conditional methods to further sort the results
     * @param param     the parameter array to decide what to values to return in the key
     * @param methods   the methods to allow further filtering of the method outputs
     * @return all the designated output based on the passed parameters for all objects
     */
    public String getMethodOuputsEx(Parameters[] param, Method... methods) {
        HashMap<String, Object> outputs = getListOfMethodOutputsEx(param, methods);
        return convertToStringArrayStyleEx(outputs, param);
    }

    /**
     * Returns a string of as much method outputs as the parameters passed for each object
     * @param param     the parameter array to decide what to values to return in the key
     * @return all the designated output based on the passed parameters for all objects
     */
    public String getMethodOuputsEx(Parameters... param) {
        HashMap<String, Object> outputs = getListOfMethodOutputsEx(param);
        return convertToStringArrayStyleEx(outputs, param);
    }

    /**
     * Returns a string of as much field values as the parameters passed for each object
     * @param param     the parameter array to decide what to values to return in the key
     * @return all the designated output based on the passed parameters for all objects
     */
    public String getFieldOutputEx(Parameters... param) {
        HashMap<String, Object> outputs = getListOfFieldOutputsEx(param);
        return convertToStringArrayStyleEx(outputs, param);
    }

    /**
     * Returns a string of as much field values as the parameters passed for each object
     * as well as allowing the passing of conditional methods to further sort the results
     * @param param     the parameter array to decide what to values to return in the key
     * @param method    the methods to allow further filtering of the method outputs
     * @return all the designated output based on the passed parameters for all objects
     */
    public String getFieldOutputEx(Parameters[] param, Method... method) {
        HashMap<String, Object> outputs = getListOfFieldOutputsEx(param, method);
        return convertToStringArrayStyleEx(outputs, param);
    }


    /**
     * Gets the names of the methods into a readable string
     *
     * @return the names of the objects methods
     */
    public String getNamesOfMethods() {
        HashMap<String, Object> outputs = getListOfMethodOutputs();
        return convertToStringArrayStyle(Arrays.asList(outputs
                .keySet().toArray(new String[outputs.keySet().size()])));
    }

    /**
     * Gets the outputs of the methods into a readable string
     *
     * @return the outputs of the objects methods
     */
    public String getObjectsOfMethods() {
        HashMap<String, Object> outputs = getListOfMethodOutputs();
        return convertToStringArrayStyle(Arrays.asList(outputs
                .values().toArray(new Object[outputs.values().size()])));
    }

    /**
     * Returns the object as an array
     * <tt>
     * example usage:
     * String[] names = new String[]{"rb", "xz", "ux", "pt", "ye"};
     * for (String name : names) {
     * try {
     * Method method = player.getClass().getMethod(name);
     * Object[] output = OutputObjectComparer.unpack(method.invoke(player));
     * log.debug("Name: " + name);
     * for (int i = 0; i (less than) output.length; i++) {
     * log.debug("Output:" + (int) output[i]);
     * }
     * log.debug("\n\n");
     * } catch (Exception e) {
     * }
     * }
     * </tt>
     * @param object the object to convert
     * @return the object as an array
     */
    public static Object[] unpack(Object object) {
        Object[] array = new Object[Array.getLength(object)];
        for (int i = 0; i < array.length; i++)
            array[i] = Array.get(object, i);
        return array;
    }


    public String[] getStringsToIgnoreInName() {
        return stringsToIgnoreInName;
    }

    public void setStringsToIgnoreInName(String[] stringsToIgnoreInName) {
        this.stringsToIgnoreInName = stringsToIgnoreInName;
    }

    public String[] getTypesToIgnore() {
        return typesToIgnore;
    }

    public void setTypesToIgnore(String[] typesToIgnore) {
        this.typesToIgnore = typesToIgnore;
    }

    public Integer[] getModifiersToInclude() {
        return modifiersToInclude;
    }

    public void setModifiersToInclude(Integer[] modifiersToInclude) {
        this.modifiersToInclude = modifiersToInclude;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String[] getTypesToInclude() {
        return typesToInclude;
    }

    public void setTypesToInclude(String[] typesToInclude) {
        this.typesToInclude = typesToInclude;
    }


    public Integer[] getParameterConstraints() {
        return parameterConstraints;
    }

    public void setParameterConstraints(Integer[] parameterConstraints) {
        this.parameterConstraints = parameterConstraints;
    }

}
