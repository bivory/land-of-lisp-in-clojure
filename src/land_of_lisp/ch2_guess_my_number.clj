(ns land-of-lisp.ch2-guess-my-number)

(def small (ref 1))
(def big (ref 100))

(defn guess-my-number-between-1-and-100
  "This function attempts to guess your number."
  []
  (str "Is your number " (bit-shift-right (+ @small @big) 1) "?"))

(defn smaller
  "Your number is smaller than the guess."
  []
  (dosync (alter big dec))
  (guess-my-number-between-1-and-100))

(defn larger
  "Your number is larger than the guess."
  []
  (dosync (alter small inc))
  (guess-my-number-between-1-and-100))

(defn yes
  "Your number was guessed correctly."
  []
  (dosync
    (ref-set big 100)
    (ref-set small 1))
  (apply str
         "Hurray! Let's play again! "
         (guess-my-number-between-1-and-100)))

