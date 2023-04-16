(ns listronica.core
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [listronica.model :as items]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]))

(def db "jdbc:postgresql://localhost/listronica?user=johndoe&password=doe")

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
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (read-string port)}))