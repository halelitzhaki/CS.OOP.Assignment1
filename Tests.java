import observer.ConcreteMember;
import observer.GroupAdmin;
import observer.JvmUtilities;
import observer.UndoableStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);
    // stub method to check external dependencies compatibility
    @Test
    public void test(){
        String s1 = "Alice";
        String s2 = "Bob";

        logger.info(()-> JvmUtilities.objectFootprint(s1));

        logger.info(()->JvmUtilities.objectFootprint(s1,s2));

        logger.info(()->JvmUtilities.objectTotalSize(s1));

        logger.info(() -> JvmUtilities.jvmInfo());
    }

    //ConcreteMember test
    @Test
    void update() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        ConcreteMember member = new ConcreteMember();
        UndoableStringBuilder usb = new UndoableStringBuilder("abcd");

        member.update(usb);
        assertEquals("String Builder new State : abcd\n", outContent.toString());
        System.setOut(originalOut);
    }

    //GroupAdmin test
    ConcreteMember member = new ConcreteMember();
    UndoableStringBuilder usb1 = new UndoableStringBuilder("abcd"), usb2 = new UndoableStringBuilder();
    GroupAdmin ga1 = new GroupAdmin(member, usb1), ga2 = new GroupAdmin(member, usb2);

    @Test
    void register() {
        GroupAdmin ga3 = new GroupAdmin();
        ga3.register(member);
        assertEquals(member.toString(), ga3.getMembers().get(0).toString());
    }

    @Test
    void unregister() {
        ConcreteMember m1 = new ConcreteMember(), m2 = new ConcreteMember();
        GroupAdmin ga3 = new GroupAdmin();
        ga3.register(member);
        ga3.unregister(member);
        assertThrows(IndexOutOfBoundsException.class, ()->{ga3.getMembers().get(0).toString();});
        ga3.register(m1);
        ga3.register(m2);
        ga3.unregister(m1);
        assertEquals(m2.toString(), ga3.getMembers().get(0).toString());
    }

    @Test
    void insert() {
        ga1.insert(2, " hello world ");
        assertEquals("ab hello world cd", ga1.getUsb().toString());
        ga1.insert(10, null);
        assertEquals("ab hello world cd", ga1.getUsb().toString());

        ga2.insert(3, "hi");
        assertEquals("", ga2.getUsb().toString()); //when the user is entering unreasonable parameters to empty string, the string stays empty
        ga2.append("hello");
        ga2.insert(-2, "hi");
        assertEquals("hello", ga2.getUsb().toString());
        ga2.insert(10, "hi");
        assertEquals("hello", ga2.getUsb().toString());
    }

    @Test
    void append() {
        ga2.append("abcd");
        assertEquals("abcd", ga2.getUsb().toString());
        ga2.append(null);
        assertEquals("abcdnull", ga2.getUsb().toString());
    }

    @Test
    void delete() {
        ga1.delete(3,4);
        assertEquals( "abc", ga1.getUsb().toString());
        ga1.delete(3,4);
        assertEquals( "abc", ga1.getUsb().toString()); //when the user is entering unreasonable parameters the string stays the same

        ga2.delete(2, 5);
        assertEquals("", ga2.getUsb().toString()); //when the user is entering unreasonable parameters to empty string, the string stays empty
        ga2.append("hello");
        ga2.delete(-2, 5);
        assertEquals("hello", ga2.getUsb().toString());
    }

    @Test
    void undo() {
        ga1.insert(2, " hello world ");

        ga1.undo();
        ga2.undo();
        assertEquals("abcd", ga1.getUsb().toString());
        assertEquals("", ga2.getUsb().toString());
    }

    @Test
    void updateAll() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        ga2.append("hello world");

        System.setOut(new PrintStream(outContent));
        ga2.updateAll();
        assertEquals("String Builder new State : hello world\n", outContent.toString());


        System.setOut(originalOut);
        ga2.register(member);
        ga2.append("!");

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ga2.updateAll();
        assertEquals("String Builder new State : hello world!\nString Builder new State : hello world!\n", outContent.toString());
        System.setOut(originalOut);
    }
}
