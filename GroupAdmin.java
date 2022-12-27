package observer;

import java.util.ArrayList;

/**
 * This class is performing the observerable part in the program.
 * It's supporting the methods in the UndoableStringBuilder class.
 * And contains a members list (ArrayList), to update them when there is a change in the UndoableStringBuilder string.
 * It implements the methods requested in the Sender interface.
 *
 * @author  Halel Itzhaki and Yehonatan Dilmoni
 * @version 1.0
 * @since   2022-12-25
 */
public class GroupAdmin implements Sender{
    private UndoableStringBuilder usb;
    private ArrayList<Member> members;

    public GroupAdmin() {
        usb = new UndoableStringBuilder();
        this.members = new ArrayList<Member>();
    }
    public GroupAdmin(UndoableStringBuilder usb) {
        this.usb = new UndoableStringBuilder(usb);
    }

    public GroupAdmin(Member member) {
        this.members = new ArrayList<Member>();
        this.register(member);
    }

    public GroupAdmin(Member member, UndoableStringBuilder usb) {
        this.members = new ArrayList<Member>();
        this.register(member);
        this.usb = new UndoableStringBuilder();
        append(usb.toString());
    }

    /**
     * This method is for register observers (members)
     * @param obj - the member to register
     * @return Nothing.
     */
    @Override
    public void register(Member obj) {
        this.members.add(obj);
    }

    /**
     * This method is for unregister observers (members)
     * @param obj - the member to unregister
     * @return Nothing.
     */
    @Override
    public void unregister(Member obj) {
        try {
            this.members.remove(obj);
        } catch (Exception e) {
            System.out.print("");
        }
    }

    /**
     * This method is using the UndoableStringBuilder's insert method, and updating the observers on the change.
     * @param offset this is the start index for the wanted substring to insert to the original string.
     * @param obj this is the wanted substring to insert to the original string.
     * @return Nothing.
     */
    @Override
    public void insert(int offset, String obj) {
        try {
            this.usb.insert(offset, obj);
            updateAll();
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is using the UndoableStringBuilder's append method, and updating the observers on the change.
     * @param obj this is the specified string to the append method.
     * @return Nothing.
     */
    @Override
    public void append(String obj) {
        try {
            this.usb.append(obj);
            updateAll();
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is using the UndoableStringBuilder's delete method, and updating the observers on the change.
     * @param start this is the start index for the wanted substring to delete.
     * @param end this is the start index for the wanted substring to delete.
     * @return Nothing.
     */
    @Override
    public void delete(int start, int end) {
        try {
            this.usb.delete(start, end);
            updateAll();
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is using the UndoableStringBuilder's undo method, and updating the observers on the change.
     *
     * @return Nothing.
     */
    @Override
    public void undo() {
        try {
            this.usb.undo();
            updateAll();
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is updating all the members on the last change in the UndoableStringBuilder.
     *
     * @return Nothing.
     */
    public void updateAll(){
        for (Member member : this.members) {
            ((ConcreteMember)member).update(usb);
        }
    }

    /**
     * This method is returning the private UndoableStringBuilder data member - usb.
     *
     * @return usb
     */
    public UndoableStringBuilder getUsb() {
        return usb;
    }

    /**
     * This method is returning the private list (data member) of the observers - members.
     *
     * @return members
     */
    public ArrayList<Member> getMembers() {
        return members;
    }
}
