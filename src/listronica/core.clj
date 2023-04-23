(ns listronica.core
  (:require [listronica.model :as items]
            [ring.adapter.jetty :as jetty]
            [listronica.handler :refer [handle-index-items
                                        handle-create-item]]
            [compojure.core :refer [defroutes GET ANY POST]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
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
           (ANY "/request" [] handle-dump)
           (GET "/items" [] handle-index-items)
           (POST "/items" [] handle-create-item)
           (not-found "Page not found!"))

(def app
  (wrap-server
    (wrap-file-info
      (wrap-resource
        (wrap-database
          (wrap-params routes))
        "static"))))

(defn -main
  [port]
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (read-string port)}))