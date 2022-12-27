package observer;
import java.util.EmptyStackException;
import java.util.Stack;


/**
 * This class is performing the requested methods of the class StringBuilder, and supporting an 'undo' method for every requested StringBuilder's method.
 *
 * @author  Halel Itzhaki and Yehonatan Dilmoni
 * @version 1.0
 * @since   2022-11-06
 */
public class UndoableStringBuilder {
    private Stack<StringBuilder> operations;

    public UndoableStringBuilder() {
        this.operations = new Stack<StringBuilder>();
    }

    public UndoableStringBuilder(String s)
    {
        this.operations = new Stack<StringBuilder>();
        operations.push(new StringBuilder(s));

    }

    public UndoableStringBuilder(StringBuilder s)
    {
        this.operations = new Stack<StringBuilder>();
        operations.push(s);
    }

    public UndoableStringBuilder(UndoableStringBuilder usb)
    {
        this.operations = (Stack<StringBuilder>)usb.getOperations().clone();
    }

    /**
     * This method is returning the private data member - operations
     * @return operations
     */
    public Stack<StringBuilder> getOperations() {
        return operations;
    }

    /**
     * This method is using the StringBuilder's append method, and saving the action's outcome in a stack to support the undo method later.
     * @param str this is the specified string to the append method
     * @return this - UndoableStringBuilder's Object. it returns the last change of the string, which was the append method.
     */
    public UndoableStringBuilder append(String str){
        try {
            StringBuilder sb = new StringBuilder();
            if(!operations.isEmpty())
            {
                sb = new StringBuilder(operations.peek());
            }
            operations.push(sb.append(str));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * this method is using the StringBuilder's delete method, and saving the action's outcome in a stack to support the undo method later.
     * @param start this is the start index for the wanted substring to delete
     * @param end this is the start index for the wanted substring to delete
     * @return this - UndoableStringBuilder's Object. it returns the last change of the string, which was the delete method.
     */
    public UndoableStringBuilder delete(int start, int end){
        if(start==end) return this;
        try
        {
            StringBuilder sb = new StringBuilder(operations.peek());
            operations.push(sb.delete(start,end));
        }
        catch(Exception e)
        {
            if(e instanceof EmptyStackException)
            {
                System.out.println("There is no string to delete.");
            }
            else if(e instanceof IndexOutOfBoundsException)
            {
                System.out.println("Range ["+ start + ", " + end +") is out of bounds");
            }
            else e.printStackTrace();
        }
        return this;
    }

    /**
     * this method is using the StringBuilder's insert method, and saving the action's outcome in a stack to support the undo method later.
     * @param offset this is the start index for the wanted substring to insert to the original string
     * @param str this is the wanted substring to insert to the original string
     * @return this - UndoableStringBuilder's Object. it returns the last change of the string, which was the insert method.
     */
    public UndoableStringBuilder insert(int offset, String str) {
        try
        {
            if(!(str == null)) {
                StringBuilder sb = new StringBuilder(operations.peek());
                operations.push(sb.insert(offset, str));
            }
        }
        catch(Exception e)
        {
            if(e instanceof EmptyStackException)
            {
                System.out.println("There is no string to insert into.");
            }
            else if(e instanceof IndexOutOfBoundsException)
            {
                System.out.println("Range ["+ offset + ", " + operations.peek().length() +") is out of bounds");
            }
            else e.printStackTrace();
        }
        return this;
    }

    /**
     * this method is using the StringBuilder's replace method, and saving the action's outcome in a stack to support the undo method later.
     * @param start this is the start index for the wanted substring to be replaced
     * @param end this is the start index for the wanted substring to be replaced
     * @param str this is the wanted substring to replace in the original string
     * @return this - UndoableStringBuilder's Object. it returns the last change of the string, which was the replace method.
     */
    public UndoableStringBuilder replace(int start,int end, String str) {
        try
        {
            if(!(str == null)) {
                StringBuilder sb = new StringBuilder(operations.peek());
                operations.push(sb.replace(start, end, str));
            }
        }
        catch(Exception e)
        {
            if(e instanceof EmptyStackException)
            {
                System.out.println("There is no string to replace in.");
            }
            else if(e instanceof IndexOutOfBoundsException)
            {
                System.out.println("Range ["+ start + ", " + end +") is out of bounds");
            }
            else e.printStackTrace();
        }
        return this;
    }

    /**
     * this method is using the StringBuilder's delete method, and saving the action's outcome in a stack to support the undo method later.
     * @return this - UndoableStringBuilder's Object. it returns the last change of the string, which was the reverse method.
     */
    public UndoableStringBuilder reverse() {
        try
        {
            StringBuilder sb = new StringBuilder(operations.peek());
            operations.push(sb.reverse());
        }
        catch(Exception e)
        {
            if(e instanceof EmptyStackException)
            {
                System.out.println("There is no string to reverse.");
            }
            else e.printStackTrace();
        }
        return this;
    }

    /**
     * this method is used to undo the last action that was performed on the object's string.
     * it's 'deleting' the last change of the string that was stored in the stack.
     * @return Nothing.
     */
    public void undo() {
        try {
            operations.pop();
        }
        catch (Exception e)
        { }
    }

    /**
     * this method is used to convert the class's data member to string,
     * so when the function System.out.println() is calling the object, it will print the object's string and not the address.
     *
     * @return string casting to the last change of the object's string
     */
    @Override
    public String toString() {
        String str = new String();
        try {
            str = operations.peek().toString();
        }
        catch(Exception e)
        { }
        return str;
    }
}
