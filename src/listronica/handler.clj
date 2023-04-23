(ns listronica.handler
  (:require [listronica.model :refer [create-item
                                      read-items
                                      update-item
                                      delete-item]]
            [listronica.view :refer [items-page]]))

(defn handle-index-items
  [request]
  (let [database  (:database request)
        items     (read-items database)]
    {:status  200
     :headers {}
     :body    (items-page items)}))

(defn handle-create-item
  [request]
  (let [name        (get-in request [:params "name"])
        description (get-in request [:params "description"])
        database    (:database request)
        item-id     (create-item database name description)]
    {:status  302
     :headers {"Location" "/items"}
     :body    ""}))