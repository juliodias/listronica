(ns listronica.handler
  (:require [listronica.model :refer [create-item
                                      read-items
                                      update-item
                                      delete-item]]))

(defn handle-index-items
  [request]
  (let [database  (:database request)
        items     (read-items database)]
    {:status 200
     :headers {}
     :body (str "<html><head></head><body><div>"
                (mapv :name items)
                "</div><form method=\"POST\" action=\"request\">"
                "<input type=\"text\" name=\"name\" placeholder=\"name\">"
                "<input type=\"text\" name=\"description\" placeholder=\"description\">"
                "<input type=\"submit\">"
                "</body></html>")}))

(defn handle-create-item
  [request]
  (let [name        (get-in request [:params "name"])
        description (get-in request [:params "description"])
        database    (:database request)
        item-id     (create-item database name description)]
    {:status  302
     :headers {"Location" "/items"}
     :body    ""}))