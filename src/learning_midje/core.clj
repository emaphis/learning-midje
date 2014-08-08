(ns learning-midje.core)

(defn first-element [sequence default]
  (if (empty? sequence)
    default
    (first sequence)))


