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

    //ConcreteMember tests
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
        logger.info(()-> JvmUtilities.objectFootprint(member, usb));
        logger.info(()->JvmUtilities.objectTotalSize(member, usb));
        logger.info(() -> JvmUtilities.jvmInfo());
    }

    //GroupAdmin tests
    @Test
    void register() {
        ConcreteMember member = new ConcreteMember();
        GroupAdmin ga = new GroupAdmin();
        ga.register(member);
        assertEquals(member.toString(), ga.getMembers().get(0).toString());
        logger.info(()-> JvmUtilities.objectFootprint(member, ga));
        logger.info(()->JvmUtilities.objectTotalSize(member, ga));
        logger.info(() -> JvmUtilities.jvmInfo());
    }

    @Test
    void unregister() {
        ConcreteMember m1 = new ConcreteMember(), m2 = new ConcreteMember(), member = new ConcreteMember();
        GroupAdmin ga = new GroupAdmin();
        ga.register(member);
        ga.unregister(member);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            ga.getMembers().get(0).toString();
        });
        ga.register(m1);
        ga.register(m2);
        ga.unregister(m1);
        assertEquals(m2.toString(), ga.getMembers().get(0).toString());
        logger.info(() -> JvmUtilities.objectFootprint(m1, m2, member, ga));
        logger.info(()->JvmUtilities.objectTotalSize(m1, m2, member, ga));
        logger.info(() -> JvmUtilities.jvmInfo());
    }

    @Test
    void insert() {
        ConcreteMember member = new ConcreteMember();
        UndoableStringBuilder usb1 = new UndoableStringBuilder("abcd"), usb2 = new UndoableStringBuilder();
        GroupAdmin ga1 = new GroupAdmin(member, usb1), ga2 = new GroupAdmin(member, usb2);
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
        logger.info(()-> JvmUtilities.objectFootprint(member, usb1, usb2, ga1, ga2));
        logger.info(()->JvmUtilities.objectTotalSize(member, usb1, usb2, ga1, ga2));
        logger.info(() -> JvmUtilities.jvmInfo());
    }

    @Test
    void append() {
        ConcreteMember member = new ConcreteMember();
        UndoableStringBuilder usb1 = new UndoableStringBuilder("abcd"), usb2 = new UndoableStringBuilder();
        GroupAdmin ga1 = new GroupAdmin(member, usb1), ga2 = new GroupAdmin(member, usb2);
        ga2.append("abcd");
        assertEquals("abcd", ga2.getUsb().toString());
        ga2.append(null);
        assertEquals("abcdnull", ga2.getUsb().toString());
        logger.info(()-> JvmUtilities.objectFootprint(member, usb1, usb2, ga1, ga2));
        logger.info(()->JvmUtilities.objectTotalSize(member, usb1, usb2, ga1, ga2));
        logger.info(() -> JvmUtilities.jvmInfo());
    }

    @Test
    void delete() {
        ConcreteMember member = new ConcreteMember();
        UndoableStringBuilder usb1 = new UndoableStringBuilder("abcd"), usb2 = new UndoableStringBuilder();
        GroupAdmin ga1 = new GroupAdmin(member, usb1), ga2 = new GroupAdmin(member, usb2);
        ga1.delete(3,4);
        assertEquals( "abc", ga1.getUsb().toString());
        ga1.delete(3,4);
        assertEquals( "abc", ga1.getUsb().toString()); //when the user is entering unreasonable parameters the string stays the same

        ga2.delete(2, 5);
        assertEquals("", ga2.getUsb().toString()); //when the user is entering unreasonable parameters to empty string, the string stays empty
        ga2.append("hello");
        ga2.delete(-2, 5);
        assertEquals("hello", ga2.getUsb().toString());
        logger.info(()-> JvmUtilities.objectFootprint(member, usb1, usb2, ga1, ga2));
        logger.info(()->JvmUtilities.objectTotalSize(member, usb1, usb2, ga1, ga2));
        logger.info(() -> JvmUtilities.jvmInfo());
    }

    @Test
    void undo() {
        ConcreteMember member = new ConcreteMember();
        UndoableStringBuilder usb1 = new UndoableStringBuilder("abcd"), usb2 = new UndoableStringBuilder();
        GroupAdmin ga1 = new GroupAdmin(member, usb1), ga2 = new GroupAdmin(member, usb2);
        ga1.insert(2, " hello world ");

        ga1.undo();
        ga2.undo();
        assertEquals("abcd", ga1.getUsb().toString());
        assertEquals("", ga2.getUsb().toString());
        logger.info(()-> JvmUtilities.objectFootprint(member, usb1, usb2, ga1, ga2));
        logger.info(()->JvmUtilities.objectTotalSize(member, usb1, usb2, ga1, ga2));
        logger.info(() -> JvmUtilities.jvmInfo());
    }

    @Test
    void updateAll() {
        ConcreteMember member = new ConcreteMember();
        UndoableStringBuilder usb1 = new UndoableStringBuilder("abcd"), usb2 = new UndoableStringBuilder();
        GroupAdmin ga1 = new GroupAdmin(member, usb1), ga2 = new GroupAdmin(member, usb2);
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
        logger.info(()-> JvmUtilities.objectFootprint(member, usb1, usb2, ga1, ga2));
        logger.info(()->JvmUtilities.objectTotalSize(member, usb1, usb2, ga1, ga2));
        logger.info(() -> JvmUtilities.jvmInfo());
    }
}
