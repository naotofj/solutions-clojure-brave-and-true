# Solutions and notes - Clojure for Brave and True

This is a repo for the (mostly mine) solutions for the exercises that are present in the awesome *Clojure for the brave and bold* book. I also included the code from the major projects from the book, which I considered worth holding on. These include *pegthing* code from the chapter 5, and *The Divine Cheese Code* from chapter 6, for example.

## Repo organization

Since this is a clojure repo for study, I've used `leiningen` for creating and run quick clojure projects, specially the bigger chapter projects. If you take a look at the file structure from this repo, you will see that it follows the basic `app` template from Leiningen.

For the major chapter projects, go to `src/`. Each chapter project will have their own `core.clj` that will then contain a `-main` function that can be called. Ex.: to run *The Divine Cheese Code*, we need to acess its namespace:

```
    $ lein run -m the-divine-cheese-code.core
```

The solutions for the end-of-chapter exercises can be found in `src/solutions`, where the solutions are separated by file. 

```
    src
     ├── ...
     ├── pegthing
     └── solutions
          ├── ...
          ├── ch_4.clj
          ├── ch_5.clj
          └── ...
```

## License

Copyright © 2019

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
