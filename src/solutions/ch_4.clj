(ns solutions.ch_4
  (:gen-class))

;; cap. 4 - example

(def filename "suspects.csv")

;;(slurp filename)
;; => "Edward Cullen,10\r\nBella Swan,0\r\nCharlie Swan,0\r\nJacob Black,3\r\nCarlisle Cullen,6"

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

;;(convert :glitter-index "3")
;; => 3
;;(convert :name "Edward Cullen")
;; => "Edward Cullen"

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\r\n")))

;;(clojure.string/split (slurp filename) #"\r\n")
;; => ["Edward Cullen,10" "Bella Swan,0" "Charlie Swan,0" "Jacob Black,3" "Carlisle Cullen,6"]

;;(parse (slurp filename))
;; => (["Edward Cullen" "10"] ["Bella Swan" "0"] ["Charlie Swan" "0"] ["Jacob Black" "3"] ["Carlisle Cullen" "6"])

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row] ;; the "map" here ensures that reduces works with only one row at a time
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

;;(mapify (parse (slurp filename)))
;; => ({:name "Edward Cullen", :glitter-index 10}
;;     {:name "Bella Swan", :glitter-index 0}
;;     {:name "Charlie Swan", :glitter-index 0}
;;     {:name "Jacob Black", :glitter-index 3}
;;     {:name "Carlisle Cullen", :glitter-index 6})

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

;;(glitter-filter 3 (mapify (parse (slurp filename))))
;; => ({:name "Edward Cullen", :glitter-index 10}
;;     {:name "Jacob Black", :glitter-index 3}
;;     {:name "Carlisle Cullen", :glitter-index 6})


;; ======================================================================
;; 1. Turn the result of your glitter filter into a list of names.

(defn glitter-filter-list
  [minimum-glitter records]
  (map :name (filter #(>= (:glitter-index %) minimum-glitter) records)))

(glitter-filter-list 3 (mapify (parse (slurp filename))))
;; => ("Edward Cullen" "Jacob Black" "Carlisle Cullen")


;; 2. Write a function, append, which will append a new suspect to your list of suspects.
(defn append
  "Append a new suspect with :name and :glitter-index to an existing map"
  [suspects new-suspect]
  (conj suspects new-suspect))

(append (mapify (parse (slurp filename))) {:name "Jacob Black" :glitter-index 0})
;; => ({:name "Jacob Black", :glitter-index 0}
;;     {:name "Edward Cullen", :glitter-index 10}
;;     {:name "Bella Swan", :glitter-index 0}
;;     {:name "Charlie Swan", :glitter-index 0}
;;     {:name "Jacob Black", :glitter-index 3}
;;     {:name "Carlisle Cullen", :glitter-index 6})


;; 3. Write a function, validate, which will check that :name and 
;; :glitter-index are present when you append. The validate function 
;; should accept two arguments: a map of keywords to validating 
;; functions, similar to conversions, and the record to be validated.

(def validators {:name string?
                 :glitter-index integer?})

(defn validate
  [to-validate record]
  (reduce
   (fn [validated [vamp-key vamp-func]] ; gets the key and associated func from validators
     (and validated (vamp-func (get record vamp-key))))
   true ; initial reduce value
   to-validate)) 

(validate validators {:name "Count Dracula" :glitter-index 99})
;; => true


;; 4. Write a function that will take your list of maps and convert 
;; it back to a CSV string. You’ll need to use the clojure.string/join function.

(def records (mapify (parse (slurp filename))))
;; records
;; => ({:name "Edward Cullen", :glitter-index 10}
;;     {:name "Bella Swan", :glitter-index 0}
;;     {:name "Charlie Swan", :glitter-index 0}
;;     {:name "Jacob Black", :glitter-index 3}
;;     {:name "Carlisle Cullen", :glitter-index 6})
 
(defn stringfy
  "Removes the keys: takes a map and returns a list of all its values"
  [record]
  (reduce (fn [result [_ val]] (conj result val))
   []
   record))

(stringfy {:name "Count Dracula" :glitter-index 99})
;; => ["Count Dracula" 99]

(map stringfy records)
;; => (["Edward Cullen" 10] ["Bella Swan" 0] ["Charlie Swan" 0] ["Jacob Black" 3] ["Carlisle Cullen" 6])

(defn unparse
  [records]
  (clojure.string/join "\n" (map #(clojure.string/join "," %) (map stringfy records))))

(unparse records)
;; => "Edward Cullen,10\nBella Swan,0\nCharlie Swan,0\nJacob Black,3\nCarlisle Cullen,6"