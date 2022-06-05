# POJES - Plain Old Java Event System

POJES is a straightforward and simple reflection-based event system for Java.


## Usage

Make a dispatch
```java
    EventDispatch dispatch = new EventDispatch();
```
Register a receiver to a dispatch
```java
class ExampleReceiver {
    //Event receivers must be public
    @EventReceiver
    public void eventReceiver() {
        
    }
}

var exampleReceiver = new ExampleReceiver();

dispatch.registerListener(exampleReceiver);
```
Dispatch an event to receivers
```java
//Events must implement IEvent, a marker interface
class ExampleEvent implements IEvent {
    public int count = 0;
}

dispatch.fireEvent(new ExampleEvent());
```

## Installation
TBA