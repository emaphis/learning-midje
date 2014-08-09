(defproject learning-midje "0.1.0-SNAPSHOT"
  :description "Learning midje for great good"
  :url "https://github.com/emaphis/learning-midje"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :plugins [[lein-midje "3.1.3"]]}})
