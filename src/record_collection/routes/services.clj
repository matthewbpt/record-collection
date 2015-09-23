(ns record-collection.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [record-collection.db.query :refer :all]
            [record-collection.db.transact :refer :all]))

(s/defschema Artist {:id Long
                     :name String
                     :bio String
                     :sortName String})

(s/defschema Album {:id Long
                    :title String
                    :year Long
                    :artists #{Long}})

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

            (POST* "/artist" []
                   :return Artist
                   :body [artist Artist]
                   (ok (do
                         (add-artist artist)
                         (get-artist (:name artist)))))))
