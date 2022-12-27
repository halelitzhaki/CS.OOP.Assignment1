package observer;

/**
 * This class is performing the observer part in the program. And contains an update method to update each member (observer).
 * It implements the method requested in the Member interface.
 *
 * @author  Halel Itzhaki and Yehonatan Dilmoni
 * @version 1.0
 * @since   2022-12-25
 */
public class ConcreteMember implements Member {
    UndoableStringBuilder usb;

    public ConcreteMember(){
        usb = null;
    }
    public ConcreteMember(UndoableStringBuilder usb) {
        this.usb = usb;
    }
    public ConcreteMember(ConcreteMember concreteMember) {
        this.usb = concreteMember.getUsb();
    }

    public UndoableStringBuilder getUsb() {
        return usb;
    }

    /**
     * Updates the observer (concrete member) on the change that was done on the string builder.
     * @param usb - the undoable string builder object
     * @return None
     */
    @Override
    public void update(UndoableStringBuilder usb) {
        this.usb = usb;
        System.out.println("String Builder new State : " + usb.getOperations().peek());
    }
}
