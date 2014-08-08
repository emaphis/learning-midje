(ns learning-midje.examples
  (:require [midje.sweet :refer :all]))


;; facts on 'facts'

(fact 3 => 3)

(fact "description" 3 => 3)

(defn add3
  "add 3 to a given num"
  [num]
  (+ 3 num))

(fact "test add3"
      (add3 4) => 7)

