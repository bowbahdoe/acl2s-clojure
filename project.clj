(defproject acl2s "0.1.0"
  :description "Macros for porting functions proved in acl2s to clojure"
  :url "mrmccue.com/acl2s-clojure"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot acl2s.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
