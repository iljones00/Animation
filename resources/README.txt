This is the README for our Animation Model made by Dallas Greene and Isaiah Jones.
We will discuss our design choices for this assignment.

We split our model into three main types of interfaces that allow us to create our animation.

First, we created an IReadOnlyShape interface for our shapes which only contains the getters for
shapes. This is because when we pass the shapes after mutation, we do not want the user to be able
to mess with fields of the shape. We then have an IShape interface extending the ReadOnly version
of the shape which also includes setters for each of these fields. These are useful because we
want to be able to mutate fields of shapes inside the model with commands, otherwise we would be
creating new objects every iteration of the animation. With thousands of shapes and thousands of
seconds to run the program, having to create a new object each tick would be a waste of memory.
We used an abstract shape class AShape to implement all of the getters and setters for each of
the fields as well as the constructor to set all of the fields as this would prevent repeated code
with each shape. The only difference between shapes should be the shape type and the way it is
drawn onto the screen. This makes it easy to create any type of shape and have it implement our
shape interface.

For the way we change or move objects we created an interface called 'ICommand'. Each command
has a start time, end time, and command type, as well as a function called "setState". The setState
method is the method that mutates the shape that assists in the animation. Given a time and a
shape, the set state method changes the shape however the command wants and returns it.
The reason we made shape a parameter of this method, instead of as a field of command was so that
we are able to use the same commands on different objects, allowing us to reuse code for different
objects. We made an abstract command class ACommand that has all of the getters and allows for
each type of command that extends the abstract to just implement the setState method.
For this assignment, we wrote a Move command that moves the given object from its string
position to its ending position with setters. We anticipate that a user may want to have possibly a
color change command, or a scale size command, which is why we created setters for each of those
fields of the shape.

As for the Animation model, our interface IAnimationModel only includes one method:
getState(int time), which should get the state of the animation at that time. We wanted to make our
model interface quite simple to allow for other implementations to be as complex or simple as one
may need. We also enforce that the getState returns a list of the read only versions of each
shape as we do not want the user to mutate the shapes of the animation after the model has dealt
with them. Then for our implementation, we made the constructor for the animation model private
so that we force a user to use our builder to create their animations. Inside the builder, we make
users add shapes with a unique ID associated with each shape. Once a shape with a certain ID is
added to the builder, the user is that allowed to add commands to the builder as long as the ID
has been added prior. Then once all of the shapes and commands have been added, we have a build
function that calls the constructor of the model. This means that once the model has been
constructed, the user is unable to mutate any of the objects that the get state method returns.
It also means that he user cannot add any more shapes or commands once the model is constructed.
This means that the user has to give all of the objects as well as the commands of each object
before the animation is created. In our implementation, we store all of the shapes and commands in
a LinkedHashmap, sorting commands based on their start times. We also allow for overlapping
commands as long as the two commands do not change the same fields. i.e. if there were a move and
change color command at the same time, those would be permitted, whereas two move commands or two
color changes at the same time would not be allowed. If this is the case, then we do not add the
second command to the model. We also override the toString() method of the
model that allows us to output a list of descriptions and movements of each object for the entire
runtime of the animation. We also have store the initial states of all of the shapes and mutate a
copy of all of the shapes so that if we want to run the animation multiple times, we do not need
to pass in all of the commands and shapes again. Plus the toString method that gets the
description of the animation moves all of the objects to their ending locations so this allows us
to be able to get a description and run the animation in the same model.


End of Assignment 5.
==================================================================================================
Start of Assignment 6.

To start this assignment, we were sure to refactor a whole bunch of our code from Assignment 5.
We overhauled our entire builder class to support the new incoming interface used to create models
that was given to us with the new code. In doing so, we had to redo how we wrote commands as we
initially divided commands up based on what fields were changing but decided to do one type of 
master command that was able to change all of the fields at once. We also created getters for each 
of the fields of the master command to facilitate this update. Another change we made was that we 
added getters for the width, height, commands, and shapes to our model, making sure that we do not 
reference the maps that the model uses to create the animation. Thus we also added an
IReadOnlyCommand interface to our model that allows us to pass the map containing commands safely 
to our view. Finally we implemented the draw() method that we left blank from the previous
assignment.

On to new code. We created one interface called IView that each of the 3 views (SVG, text, and visual)
all fall under, that has one shared method called play(). This method either starts the animation in
the visual case, or creates the appendable containing the text that represents the animation in the 
text cases. We also created another interface for SVG and text view called ITextView with a 
unique method String getText(). This method returns the string that represents the animation as a text,
whether in the format for a description, or in the format in XML to be viewed in a browser as an SVG.

The TextView is the most simple class, implementing the play as well as the getText method in the 
format we created. The play simply initializes the appendable that goes through the shapes and 
commands of the given model, and outputs a string that contains the starting and ending information
for the shape that goes through the given command. Changing the speed of the TextView does nothing
as all the getText method does is return what the animation does tick by tick, disregarding
how often ticks are processed.

The SVG view is also fairly simple. It once again takes a model and gets its commands and shapes
and creates text that is in the XML format representing the movement of each of the shapes. 
Its constructor does take in a tick speed and allows the user to speed up the animation if they so
desire. The animation runs once on the screen before freezing as the last shape finishes its 
final command. It initializes each shape with the correct tags and at the correct location and the 
writes each of the commands of the shape with the <animate> tag. Then once all of the commands have 
finished being processed, the shape gets closed and then the next shape and its commands get written.
Then the string builder finishes by writing the closing tag </svg>

Finally, the Visual view uses javax swing to create a GUI that plays the animation. It takes a model
and a ticks per second parameter that sets the speed of the animation. The window that the animation
is played in is set initially to the width and height specified in the model, and has scroll bars so
the user can scroll to wherever they choose. The panel on which the animaiton is drawn is set to be
exactly big enough to fit all shapes at all times.

On top of writing the three views, we also wrote a main class called 'Excellence', in no subtle nod 
to the quality of our work. In our main method, we look through the given parameters, allowing
the user to input how they want the given animation to be run. We enforce that -view and -in 
must be present and must have a proper view and input file to be read. Then we also look for 
-speed, which changes the tick speed of the animation to the given tickspeed, and -out, which 
allows the user to write a file with the given file name. Each of these throw a pop-up error
if the user does not give a proper item following the tag, whether that is no field specified, or 
just a field of an incorrect type. The once all of the fields are created correctly, the main
creates a view of the specified class and creates an animation. Then the animation runs and all is well. 

End of Assignment 6.
=========================================================================================================
Start of Assignment 7.

Woo boy. This was a tough assignment. So to start assignment 7, we decided to refactor some of our code
to get it to work with the new stuff. We added a keyframe class to help with our editing view. We added
an interface called IEditableView that has a bunch of edit methods as well as set action listener methods. 
Then we added getters for the shapes and for the commands inside of the builder so we are able to see
the fields from the inside of the builder. We extended the previously existing builder interface with 
a new builder interface called IEditBuilder that includes these getters and the remove command and 
remove shape methods that we did not implement before. Then we changed the inside of our main class
to include a check for "edit" field being passed in to create our editing view. 

As for new code, we created an interface called IController that contains all of the methods of our controller
implementation. We only gave it one method in the interface void go() that takes no arguments and creates
and runs the controller. This is to allow for as open of implementations as anyone can think of as 
the controller does not need to be tightly coupled to anything else for the animation to run. Inside of our
controller, we had the contructor take the editable animation builder, an editable view, and a tick speed.
This is so that we are able to control the builder from the view itself, as we enforce it that the model 
cannot be edited once it is initialized. This way we are able to add and remove shapes and commands before the
animation is run. If the animation has been built and the user wants to edit their animation, we rebuild the
model for the animation with the new fields. 

We also created several action event types and action listeners
for our implementation as we felt that the existing actionListener interface and actionEvent classes do
not express our code thoroughly. Thus, we created a shapeChangeEvent, a frameChangeEvent, and used property
change event and action evsnts to send user inputs to the controller. We then implemented 
ActionListener, IFrameChangeListener, IShapeChangeListener, and PropertyChangeListener in the controller to 
allow for the controller to receive these inputs. Then, the controller now has the getters of the fields 
for each of these classes and switches based on the enums ADD and DELETE for shape change events.getType(), 
and ADD, EDIT, and DELETE for the keyframe change event. We used the propertyChange events to allow the 
user to both change the speed of the animation, as well as export the animation as an svg file by creating 
the previous svg view from the editing view. And then from actionPerformed method, we look for the different
buttons someone can press, which are 'play', 'pause', 'loop', and 'restart'. It also has a click to export as 
svg field that enables the user to create an svg file out of their created animation. Each of them 
send an action event that contains that field that then allows the controller to switch on 
the type of the action event. 

The editing view is also its own action listener and listSelectionListener as there are events that occur
that we should deal with within the view. These are cases like clicking a shape or keyframe and updating
that shape cell or keyframe cell to change color to indicate selection. It also deals with opening popup
menus and has a handler to fire various types of events to itself or the controller to deal with. 

The IEditView interface also contains add property, shapechange, and keyframe change listener methods
to allow the controller to add itself to the view's list of event listeners. 

Because we did not implement keyframes 
in the original model, we decided to convert from the commands in the model, into keyframes in the view.
This means that when we receive the mappings of id to commands from the model, we must convert that into
id to keyframes in the view for the view to implement. Because of this, editing a keyframe meant that we 
had to delete the command from the model and add commands in their place to signal the new keyframe in the
model. 

Then to display the editing view to the user, we overlayed panels upon panels. We used the center panel for
the animation, overlaying the button toolbar on that, the shapes tool bar on the left on top of that, and 
then the keyframes tool bar on the right on top of that. For both the shapes and the keyframe toolbar,
we used the grid layout again to divide them into three, with a title (either SHAPES or KEYFRAMES), the
selection part which allows the user to select a shape or keyframe, and the buttons they can press,
which are add, remove for shapes and add, edit, and remove for keyframes. When the user presses one of 
these buttons, a pop up menu opens that allows the user to input their desired changes or additions. 

We deal with errors in a good way I feel. Whenever a user inputs a bad number into a field, or tries to 
do an operation not supported by our implementation, we call the displayError message that contains 
helpful information describing what failed. This is so that our animation continues to run, even if the user
attempts to give negative values for width for example, or tries to give a negative tick speed.

Once the controller is created and called, inside its go method, it initializes all of the fields of 
the view, adding itself to the list of action listeners, and setting the height, width, tickspeed, 
keyframes, and shapes of the view before calling its own play method. The play() method uses the builder 
contained inside the controller to build a model based on the created commands. It then schedules a timer
based on the tickspeed given and builds an animation for the user. 

The main reason we chose to pass a builder to the controller versus a completed model was so that the animation
itself runs smoothly, while the building of the animation takes more time. We felt that it is more important
for the animation to run smoothly rather than the building of the animation itself and it appears to be the case. 

Anyways, if you do get to the end of this file, thanks for grading and everything. Hope you enjoyed our 
animation and have a great rest of your summer. Peace.

End of Assignment 7
================================================================================================================
B-B-B-B-Bonus Assignment!!!!

Heyo you thought we were done!! We were. But wait theres more!! For this new assignment, we made quite a few
changes to our original code to work. 

Pt. 1 Slider
To get the slider to work, we added a case in the property change switch statement that gets the value of the 
slider as a percentage, then multiplied that out into the current tick of the animation, pausing the video
when updated. We also had to do something similar in the vice versa case where if the animation is playing,
the slider gets moved proportionately as well. We had to do some tricky boolean logic to check if the slider 
is being moved by us or the controller as we came across a bug where the slider updated the controller updated 
the slider and looped infinitely, sticking the animation at a given tick. Then we added a slider on the bottom
of our visual panel to allow for the user to move the slider.

Pt. 2 Rotations
In order to add rotations to our animation we had to change our existing implementions of command and shapes.
We extended the master command class in a new class called super master command with orientations. 
We updated our shapes to include orientations and getters for orientations, then got all of our getters 
in the model and view to work with these new shapes and commands. We had to update our edit view to also 
include an orientation field in the pop up bar to allow a user to type in their desired key frame orientation
for their animation. Then for the svg view, we made it so that only rotations can happen if there is no movement
as well as we were not able to figure out how to write an svg with rotations and movements at the same time. 
We updated our text view to also include an oriention of the shape upon printing out commands. And for our 
animation reader, we check if the file has 18 integers instead of 16 integers, allowing us to input orientations
and have the reader understand both the previous written files, as well as new files that also include an orientation
starting and ending field in their documents. We also had an export to svg view button before that worked so 
our edit view was already able to export a file as an svg which you will see later.

Thank you so much for reading and have a great summer for real this time.
