(ns record-collection.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [record-collection.db.query :refer :all]
            [record-collection.db.transact :refer :all]
            [ring.swagger.upload :as upload]
            [clojure.java.io :as io]))

(s/defschema Artist {:id Long
                     :name String
                     :bio String
                     :sortName String})

(s/defschema Album {:id Long
                    :title String
                    :year Long
                    :artists #{Long}
                    :album-cover-id (s/maybe Long)})

(defn to-byte-array [file]
  (with-open [input (new java.io.FileInputStream file)
              buffer (new java.io.ByteArrayOutputStream)]
    (io/copy input buffer)
    (.toByteArray buffer)))

(defapi service-routes
  (ring.swagger.ui/swagger-ui
   "/swagger-ui")
  ;JSON docs available at the /swagger.json route
  (swagger-docs
    {:info {:title "Record Collection"}})
  (context* "/api" []
            :tags ["API"]

            (GET* "/artists" []
                  :return [Artist]
                  (get-artists))

            (GET* "/artist/:name" []
                  :return Artist
                  :path-params [name :- String]
                  (ok (get-artist name)))

            (GET* "/artist/:name/albums" []
                  :return [Album]
                  :path-params [name :- String]
                  (ok (get-albums name)))

            (GET* "/albums" []
                  :return [Album]
                  (ok (get-albums)))

            (GET* "/image/:id" []
                  
                  :path-params [id :- Long]
                  (ok (new java.io.ByteArrayInputStream (get-image id))))
            
            (POST* "/artist" []
                   :return Artist
                   :body [artist Artist]
                   (ok (do
                         (add-artist artist)
                         (get-artist (:name artist)))))
            (POST* "/album" []
                   :return Album
                   :body [album Album]
                   (ok (do
                         (add-album album)
                         (get-album (:title album)))))
            
            (POST* "/album/:id/cover" []
                   :return Long
                   :multipart-params [file :- upload/TempFileUpload]
                   :path-params [id :- Long]
                   :middlewares [upload/wrap-multipart-params]
                   (ok (do 
                           (add-album-cover id (assoc file :bytes (to-byte-array (:tempfile file))))
                           (get-cover-id id))))
            ))
