# CS.OOP.Assignment1

## Installation

Use the [git_link](https://github.com/halelitzhaki/CS.OOP.Assignment1.git) to install CS.OOP.Assignment1.


## Usage

```java
import observer.*;

// create concrete member (observer) data member
ConcreteMember member = new ConcreteMember();

// create undoable string builder data member
UndoableStringBuilder usb = new UndoableStringBuilder();

// create group admin (observerable) data member
GroupAdmin groupAdmin = new GroupAdmin(member, usb);

// updates observers
groupAdmin.updateAll();

// append string to the UndoableStringBuilder data member and update observers
groupAdmin.append("hello world");

// performs undo action on the UndoableStringBuilder data member  and update observers
groupAdmin.undo();
```
