(ns listronica.core
  (:require [listronica.model :as items]
            [ring.adapter.jetty :as jetty]
            [listronica.handler :refer [handle-index-items]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]))

(def db "jdbc:postgresql://localhost/listronica?user=johndoe&password=doe")

(defn home
  []
  {:status 200
   :body "Hello, World!"
   :headers {}})

(defn wrap-database
  [handler]
  (fn [request]
    (handler (assoc request :database db))))

(defn wrap-server
  [handler]
  (fn [request]
    (assoc-in (handler request) [:headers "Server"] "Listronica 8000")))
(defroutes routes
           (GET "/" [] (home))
           (GET "/items" [] handle-index-items)
           (GET "/request" [] handle-dump)
           (not-found "Page not found!"))

(def app
  (wrap-server
    (wrap-database
      (wrap-params routes))))

(defn -main
  [port]
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (read-string port)}))