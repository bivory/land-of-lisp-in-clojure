(defproject land-of-lisp "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]]

  :dev-dependencies [[jline "0.9.94"]
                     ;; Documentation
                     [org.clojars.weavejester/autodoc "0.9.0"]

                     ;; Benchmarking
                     ;; https://github.com/hugoduncan/criterium
                     ;;[criterium "0.1.0"]

                     ;; BDD
                     ;; https://github.com/marick/Midje/
                     [midje "1.3.0"]
                     [lein-midje "1.0.7"]

                     ;; Vim
                     ;; https://bitbucket.org/kotarak/vimclojure
                     [vimclojure/server "2.3.1"]
                     [org.clojars.autre/lein-vimclojure "1.0.0"]

                     ;; Finding functions
                     ;; https://github.com/Raynes/findfn
                     [findfn "0.1.1"]]

  :jar-exclusions [#"(?:^|/).git"]
  :project-init (require 'clojure.pprint))
