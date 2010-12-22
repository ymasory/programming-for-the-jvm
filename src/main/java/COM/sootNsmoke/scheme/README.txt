This is an implementation of the Scheme R4 specification, using the
Java Virtual Machine. 

All files copyright (C) 1999 by Joshua Engel
These files are made available under the GNU Public License.

To start the interpreter: 
java COM.sootNsmoke.scheme.ReadEvalPrint

To read in the Scheme R4 library:
(load "library.scm")

To exit:
(quit)

************************************************************************

This is a nearly complete implementation of the Scheme R4
specification.  

The R4 library is written in a combination of Scheme and Java.  Those
parts written in Java are loaded automatically.  The parts written in
Scheme are part of the "library.scm" file, which can be loaded.  

call-with-current-continuation is missing.  While it could be
implemented, it would involve a complete rewrite of the way the code
works.  For an example of how you might do that, refer to the book to
see how to implement Sather iterators in chapter 11.

You can also use Java exceptions to implement some of the
call-with-current-continuation behavor.  call/cc is often used for
exceptions, but it is somewhat more powerful than that.

All of the rest of Scheme R4 should be available, including the entire
numerical package.  If there are any parts missing, contact me and
we'll see what we can do.  Better yet, read the book and try to
implement them yourself.
