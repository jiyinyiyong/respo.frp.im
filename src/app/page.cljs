
(ns app.page
  (:require [respo.render.html :refer [make-string]]
            [app.comp.container :refer [comp-container]]
            [app.schema :as schema]
            [shell-page.core :refer [make-page spit slurp]]
            [cljs.reader :refer [read-string]]
            [app.config :as config]
            [cumulo-util.build :refer [get-ip!]])
  (:require-macros [clojure.core.strint :refer [<<]]))

(def base-info
  (merge
   {:title (:title config/site),
    :icon (:icon config/site),
    :ssr nil,
    :inline-styles [(slurp "entry/github-gist.css")]}))

(defn dev-page []
  (make-page
   ""
   (merge
    base-info
    {:styles [(<< "http://~(get-ip!):8100/main.css") "/entry/main.css"],
     :scripts [{:src "/client.js", :defer? true}],
     :inline-styles [(slurp "entry/github-gist.css")]})))

(def ga-html (slurp "entry/ga.html"))

(defn prod-page []
  (let [html-content (make-string (comp-container schema/store))
        assets (read-string (slurp "dist/assets.edn"))
        cdn (if config/cdn? "" (:cdn-url config/site))
        prefix-cdn (fn [x] (str cdn x))]
    (make-page
     (str html-content ga-html)
     (merge
      base-info
      {:styles [(:release-ui config/site)],
       :scripts (map (fn [x] {:src (-> x :output-name prefix-cdn), :defer? true}) assets),
       :ssr "respo-ssr",
       :inline-styles [(slurp "entry/github-gist.css") (slurp "./entry/main.css")]}))))

(defn main! []
  (println "Running mode:" (if config/dev? "dev" "release"))
  (if config/dev?
    (spit "target/index.html" (dev-page))
    (spit "dist/index.html" (prod-page))))

(main!)
