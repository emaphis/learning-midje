(ns learning-midje.checkables
  (:require [midje.sweet :refer :all]
;            [midje.test-util :refer :all]  ;; TODO: fix namespace problem
            [midje.config :as config]))

;;; from the [midje guide](https://github.com/marick/Midje/wiki/Checkables)
;;;

;; a checkable consists of three parts:
;; left-hand-expression => right-hand-expression

;; the left hand part is the expression being tested

;; Two forms of right hand exptressions of checkables are 
;; a. functions - the results of evaluation of the left hand side are given
;;    to the function on the right hand side. The checkable is true in the
;;    reults up the function are truthy

(fact 3 => odd?)
(fact 4 => even?)
(fact 4 => integer?)

;; b. regular expressions:
(comment
  (f) => #"foo+"                  ; this checkable has the same meaning
  (re-find #"foo+"(f)) => truthy  ; as this expession

  truthy ; is a checker that returns true given any value Clojure counts as true.
  )

(fact "cat" => #"c+a+t")
(fact "dog" => #"d*g")


;; two kinds of arrows: => returns true if right hand evaluates true,
;; =not=> or =deny=>  succed if right evaluates not truthy
(fact 1 => odd?)
(fact 1 =not=> even?)
(fact "cat" =deny=> #"d*g")




;;; several kind of checkers checkers:

;;; simple checkers:

;;; predefined:

;; truthy or falsey



;;; checking sequential collections
;;; https://github.com/marick/Midje/wiki/Checking-sequential-collections
;;; https://github.com/marick/Midje/blob/1.6/test/as_documentation/checkers__for_sequences.clj
;; just, contains, has-prefix, has-suffix, n-of

;; sequences are checked for equality so lists, vectors, lazy sequences can be equal
(fact "As usual in Clojure, the type doesn't matter"
  '(1 2 3) => [1 2 3]
  [1 '(2) 3] => '(1 [2] 3)
  (map inc (range 0 3)) => [1 2 3])


;; just - the actual and expected results must have the same number of elements.
(fact "just uses extended equality"
  [1 2 3] => (just [odd? even? odd?])
  ["a" "aa" "aaa"] (just [#"a+" #"a+" #"a+"]))

(fact
  "as a side note, you can also use `three-of` instead of the previous checkable"
  ["a" "aa" "aaa"] => (three-of #"a+"))

(fact "when `just` takes no options, you can omit brackets"
  [1 2 3] => (just odd? even? odd?))

;; Since just always takes a collection, wrapping the expected collection in
;; brackets or quoted parentheses is redundant.
(fact "when `just` takes no options, you can omit brackets"
  [1 2 3] => (just odd? even? odd?))

;; just isn't as convenient for trees as it is for flat lists because extended
;; equality in just is not recursive.
(fact "extended equality only applies to the top level"
  [[[1]]] =not=> (just [[[odd?]]]))

;; for trees or embedded structures:
(fact "you have to do this"
  [[[1]]] => (just (just (just odd?))))


;;; order

;; if you care about contents but not order, use a set in 'just'.
(fact [2 1 3] => (just #{1 2 3}))
(fact [2 1 3] => (just #{2 3 1}))
(fact [2 1 3] => (just #{3 1 2}))

;; OR.
(fact "you can specify that order is irrelevant"
  [1 3 2] => (just [1 2 3] :in-any-order))

(fact "and you can even leave out the brackets"
  [2 1 3] => (just 1 2 3 :in-any-order))

(fact "Midje tries not to be fooled by committing to too-exact matches"
  [1 3] => (just [odd? 1] :in-any-order))

; If Midje decided that the actual value 1 matched odd?, then a mismatch 
; of 3 and the 1 after odd? would lead to a spurious failure. But Midje
;  correctly discovers the match in the alternate order.
;; WOW!


;;; contains - the actual result may have extra elements

;; contains searches for a matching subsequence of the actual results:
(fact "contains requires only a subset to match"
  [1 2 3] => (contains even? odd?))

;; you may also omit brackest


(fact "contains requires a contiguous match"
  [1 2 3] =not=> (contains odd? odd?))

;; If you want to relax that requirement, use :gaps-ok:
(fact "... but you can avoid that with :gaps-ok"
  [1 2 3] => (contains [odd? odd?] :gaps-ok))

;; contains, like just, supports :in-any-order. You can also use a set if you
;; want to be more terse:
(fact ":in-any-order or set arguments are also supported"
  [5 1 4 2] => (contains [1 2 5] :gaps-ok :in-any-order)
  [5 1 4 2] => (contains #{1 2 5} :gaps-ok))



;;; has-prefix and has-suffix for anchored subsequences

;;  contains finds matches anywhere within the sequence, has-prefix
;; forces the match to be at the beginning:
(fact "has-prefix is anchored to the left"
    [1 2 3] =not=> (has-prefix [2 3])     ; it's not a prefix
    [1 2 3] =>     (has-prefix [1 2])     
    [1 2 3] =not=> (has-prefix [2 1])     ; order matters
    [1 2 3] =>     (has-prefix [2 1] :in-any-order)
    [1 2 3] =>     (has-prefix #{2 1}))   ; sets work too.

;; has-suffix is like has-prefix, except the match has to be at the very end.
(fact "has-suffix is anchored to the left"
  [1 2 3] => (has-suffix [2 3])
  [1 2 3] =not=> (has-suffix [1 2]) ; not a suffix
  [1 2 3] =not=> (has-suffix [3 2]) ; order matters
  [1 2 3] => (has-suffix [3 2] :in-any-order)
  [1 2 3] => (has-suffix #{3 2}))   ; and sets


;;; n-of and friends

;; These are used to avoid giving just a sequence of n identical elements:
(fact "one checker to match exactly N elements."
  ["a"] => (one-of "a")
  [:k :w] => (two-of keyword?)
  ["a" "aa" "aaa"] => (three-of #"a+")
  ;; ...
  [1 3 5 7 9 11 13 15 17] => (nine-of odd?)
  ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j"] => (ten-of string?)

  ;; to go above ten-of
  (repeat 100 "a") => (n-of "a" 100))

(fact "counts must be exact"
  ["a" "a" "a"] =not=> (one-of "a")
  [:k :w :extra :keywords] =not=> (two-of keyword?))


;; n-of uses extended equality, so you can check arrays of numbers:
(fact "the result is an even square"
  [[2 4]
   [6 8]] => (two-of (two-of even?)))


;;; has - checks a propery agains some or all of sequence values

;; has -- some, every?
;; You can apply Clojure's quantification functions (every?, some, and so on)
;; to all the values of a sequence:
(fact "has applies clojure quantification functions to all values a sequence"
  [2 3 4] => (has some odd?)
  [2 4 6] =not=> (has some odd?)
  [2 3 4] =not=> (has every? even?)
  [2 4 6] => (has every? even?))

;; it appears you can do the same with partial:
(fact "this is not that different than `partial`..."
  [2 3 4] => (partial some odd?)
  [2 4 6] =not=> (has some odd?))

;; BUT, There are two reasons to use has instead:

;; a. you can use regular expressions:
(fact
  ["ab" "aab" "aaab"] => (has every? #"a+b"))
(fact
  [1 2 3] => (has not-any? #"a+b"))

;; b. Some more elaborate uses of partial will not work as you expect:
(comment
  (fact [[1] [2]] => (partial every? (just [1]))))
;; This incorrectly checks out because of how the collection checkers
;; report failures. 
