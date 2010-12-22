Copyright (c) 1999 by Joshua Engel
Covered under the GNU General Public License

This is a simple Prolog compiler.

Compile files for use by running the PrologCompiler.  Give it file
names on the command line.  The prolog file is compiled into a series
of class files.  There is a separate file for each predicate, and the
arity is included.  Thus, if you have a sequence of definitions:

within(maryland, usa).
within(laurel, maryland).

You get a file called within_2.class, where 2 is the arity of the predicate.

Once files are compiled, you can use the trivial Prolog interpreter
COM.sootNsmoke.prolog.Prolog.  It produces a prompt:

?- 

The Prolog interpreter understands only single predicates, not
conjunctions.  Thus, you can see where Laurel is by asking:

within(laurel, X).

and the system will respond:

X = maryland.

For another example, look in tests/test.P at the top level of this
distribution.


