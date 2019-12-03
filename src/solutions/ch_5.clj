(ns solutions.ch_5
  (:gen-class))

;; 1. You used (comp :intelligence :attributes) to create a function that returns a 
;; character’s intelligence. Create a new function, attr, that you can call like 
;; (attr :intelligence) and that does the same thing.

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(defn attr 
  [attribute]
  (fn [char] (attribute (:attributes char)))
  )

((attr :intelligence) character)
;; => 10

(def c-int (attr :intelligence))
(c-int character)
;; => 10


;; 2. Implement the comp function.

;; "Clojure’s comp function can compose any number of functions. 
;; Implementation that composes just two functions:"


(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

;; reimplementing Clojure’s comp function to compose any number of functions.
;; (not mine, kudos to the stackoverflow guy)

(defn my-comp [& fs] ; fs is a list of funcs 
  (reduce (fn [result f]
            (fn [& args]
              (result (apply f args))))
          identity
          fs))

; Notes: Reduce consumes the fs (funcs list).
; The reducer function has two params: 
; - f, an element of the fs list (func_n, func_n-1, func_n-2, ... )
; - result, a composition of funcs. 
; Note that reducer func evaluates to another fn, so the 
; first evaluation of the reduce will be 
; (fn [& args] (identity (apply func_n args)) -> this will be the "next result"
; then the next evaluation of the next f will be
; (fn [& args] (fn [& args] (identity (apply func_n args) (apply func_n-1 args))
; and so on.


;; 3. Implement the assoc-in function. Hint: use the assoc function and define 
;; its parameters as [m [k & ks] v].

; assoc-in works like:
; (assoc-in {} [:cookie :monster :vocals] "Finntroll")
; => {:cookie {:monster {:vocals "Finntroll"}}}
;
; Expanding the example:
; (assoc {} :cookie (assoc {} :monster (assoc {} :vocals "Finntroll")))
; (fn [key map] (assoc {} key (inner-map)))
;
; so:
; m = {} - an initial map
; [k & ks] = [:cookie :monster :vocals] - a vector of keys 
; v = "Finntroll" - the value to append to the last key of the vector of keys

(defn my-assoc-in
  [m [k & ks] v]
  ((reduce (fn [result key]
             (fn [inner-map] (result (assoc {} key inner-map))))
           (fn [val] (assoc m k val))
           ks) v))

(my-assoc-in {} [:cookie] "Monster")
;; => {:cookie "Monster"}

(my-assoc-in {} [:cookie :monster :vocals] "Finntroll")
;; => {:cookie {:monster {:vocals "Finntroll"}}}

; Notes: This one I solved myself. The logic behind it is the same as from
; problem 2, though. This is the core version:

(defn core-assoc-in
  ;; metadata elided
  [m [k & ks] v]
  (if ks
    (assoc m k (core-assoc-in (get m k) ks v))
    (assoc m k v)))


;; 4. Look up and use the update-in function.

(def test-map  {:cookie {:monster {:vocals "Finntroll"}}})

(update-in test-map [:cookie :monster :vocals] #(str % " Nifelvind"))
;; => {:cookie {:monster {:vocals "Finntroll Nifelvind"}}}


; 5. Implement update-in.

; This is from clojure.core:

(defn core-update-in
  ([m ks f & args]
   (let [up (fn up [m ks f args]
              (let [[k & ks] ks]
                (if ks
                  (assoc m k (up (get m k) ks f args))
                  
                  (assoc m k (apply f (get m k) args)))))]
     (up m ks f args))))

(core-update-in test-map [:cookie :monster :vocals] #(str % " Nifelvind"))
;; => {:cookie {:monster {:vocals "Finntroll Nifelvind"}}}

