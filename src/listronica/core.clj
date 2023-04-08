(ns listronica.core
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn home
  []
  {:status 200
   :body "Hello, World!"
   :headers {}})
(defroutes app
           (GET "/" [] (home))
           (route/not-found "Page not found!"))

(defn -main
  [port]
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (read-string port)}))