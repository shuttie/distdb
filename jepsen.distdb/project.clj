(defproject jepsen.distdb "0.1.0-SNAPSHOT"
  :description "A jepsen test for sample distributed database"
  :url "http://github.com/shuttie/distdb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [jepsen "0.0.9"]
                 [clj-http "2.0.1"]])
