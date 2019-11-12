(ns solutions.ch_3
  (:gen-class))

;; cap. 3 - example

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
      (loop [[part & remaining] sym-parts
             accumulated-size (:size part)]
        (if (> accumulated-size target)
          part
          (recur remaining (+ accumulated-size (:size (first remaining))))))))


;; ======================================================================
;; 1. Use the str, vector, list, hash-map, and hash-set functions.

(str "This is the solution for exercise number " 1)
;; => "This is the solution for exercise number 1"

(vec '(1 "one" 100 "one-hundred"))
;; => [1 "one" 100 "one-hundred"]

(list "two" 2 "200" "two-hundred")
;; => ("two" 2 "200" "two-hundred")

(hash-map :animal "dog" :age 2 :name "Mr. Bones")
;; => {:age 2, :animal "dog", :name "Mr. Bones"}

(hash-set 1 2 3 3 3 4 4 5 6 "test" "test")
;; => #{1 2 3 4 5 6 "test"}


;; 2. Write a function that takes a number and adds 100 to it.

(defn add-100
  [n]
  (+ 100 n))

(add-100 5)
;; => 105


;; 3. Write a function, dec-maker, that works exactly like the 
;; function inc-maker except with subtraction:

(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))

(def inc3 (inc-maker 3))

(inc3 7)
; => 10        

(defn dec-maker
  "Create a custom decrementor"
  [dec-by]
  #(- % dec-by))

(def dec3 (dec-maker 3))

(dec3 10)
;; => 7

;; 4. Write a function, mapset, that works like map except the return value is a set:
(defn mapset
  [func & colls]
  (set (apply map func colls)))

(mapset inc [1 1 2 2])


;; 5. Create a function that’s similar to symmetrize-body-parts except that it has 
;; to work with weird space aliens with radial symmetry. 
;; Instead of two eyes, arms, legs, and so on, they have five.

(def asym-hobbit-body-parts-2 [{:name "head" :size 3}
                             {:name "first-eye" :size 1}
                             {:name "first-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "first-shoulder" :size 3}
                             {:name "first-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "first-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "first-kidney" :size 1}
                             {:name "first-hand" :size 2}
                             {:name "first-knee" :size 2}
                             {:name "first-thigh" :size 4}
                             {:name "first-lower-leg" :size 3}
                             {:name "first-achilles" :size 1}
                             {:name "first-foot" :size 2}])

(defn matching-part-2
  [part]
  (if (re-find #"first" (:name part))
    (let [radial_parts ["second-" "third-" "forth-" "fifth-"]
          current_part (re-find #"\w[^-]*$" (:name part))]
      (reduce (fn [final_parts radial_prefix]
                (conj
                 final_parts
                 {:name (str radial_prefix current_part) :size (:size part)}))
              []
              radial_parts))
    [part]))

(defn better-symmetrize-body-parts-2
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (distinct (into [part] (matching-part-2 part)))))
          []
          asym-body-parts))

(defn hit-2
  [asym-body-parts]

  (let [sym-parts (better-symmetrize-body-parts-2 asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

;;(better-symmetrize-body-parts asym-hobbit-body-parts)

(hit-2 asym-hobbit-body-parts)
;; => {:name "left-forearm", :size 3}


;; 6. Create a function that generalizes symmetrize-body-parts and the function
;; you created in Exercise 5. The new function should take a collection of body 
;; parts and the number of matching body parts to add.

(def asym-hobbit-body-parts-3 [{:name "head" :size 3}
                             {:name "eye-1" :size 1}
                             {:name "ear-1" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "shoulder-1" :size 3}
                             {:name "upper-arm-1" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "forearm-1" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "kidney-1" :size 1}
                             {:name "hand-1" :size 2}
                             {:name "knee-1" :size 2}
                             {:name "thigh-1" :size 4}
                             {:name "lower-leg-1" :size 3}
                             {:name "achilles-1" :size 1}
                             {:name "foot-1" :size 2}])

(defn matching-part-3
  [part target]
  (if (re-find #"-1" (:name part))
    (let [radial_parts (into [] (range 1 target))
          current_part (re-find #"^[^1]*" (:name part))]
      (reduce (fn [final_parts radial_prefix]
                (conj
                 final_parts
                 {:name (str current_part radial_prefix) :size (:size part)}))
              []
              radial_parts))
    [part]))

(defn better-symmetrize-body-parts-3
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts no-of-members]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (distinct (into [part] (matching-part-3 part no-of-members)))))
          []
          asym-body-parts))

(defn hit-3
  [asym-body-parts no-of-members]

  (let [sym-parts (better-symmetrize-body-parts-3 asym-body-parts no-of-members)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

;;(better-symmetrize-body-parts-3 asym-hobbit-body-parts-3 3)

(hit-3 asym-hobbit-body-parts-3 3)
;; => {:name "chest", :size 10}

