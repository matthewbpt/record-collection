(ns record-collection.handlers
  (:require-macros [reagent.ratom :refer [reaction]]
                   [cljs.core.async.macros :refer (go)])
  (:require [re-frame.core :refer [register-handler
                                   register-sub
                                   dispatch
                                   subscribe]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<! >! chan]]
            [clojure.string :refer [blank?]]))

(register-handler
  :initdb
  (fn [db _]
    (merge db { :filter "" :current-artists [] :current-artist nil })))

(register-handler
  :filter
  (fn [db [_ filter]]
    (merge db {:filter filter})))

(register-handler
  :get-artists
  (fn [db _]
    (go (let [response (<! (http/get "/api/artists" {:headers {"Accept" "application/edn"}}))
              artists (:body response)
              status (:status response)]
          (if (= status 200)
            (dispatch [:current-artists artists]))))
    db))

(register-handler
  :albums
  (fn [db [_ albums]]
    (merge db {:albums albums})))

(register-handler
  :get-albums
  (fn [db [_ artist]]
    (let [url (if (nil? artist)
                "/api/albums"
                (str "/api/artist/" artist "/albums"))]
      (go (let [response (<! (http/get url {:headers {"Accept" "application/edn"}}))
                albums (:body response)
                status (:status response)]
            (if (= status 200)
              (dispatch [:albums albums])))))
    db))

(register-handler
  :current-artists
  (fn [db [_ artists]]
    (merge db {:current-artists artists})))

(register-handler
  :current-artist
  (fn [db [_ artist-name]]
    (merge db {:current-artist artist-name})))

(register-handler
  :add-artist-response
  (fn [db [_ artist]]
    (merge db {:current-artists (cons artist (:current-artists db))})))

(register-handler
  :add-artist
  (fn [db [_ artist]]
    (go (let [response (<! (http/post "/api/artist" {:edn-params artist :headers {"Accept" "application/edn"}}))
              status (:status response)
              artist (:body response)]
          (if (= status 200)
            (dispatch [:add-artist-response artist]))))
    db))

(register-handler
  :add-album-response
  (fn [db [_ album]]
    (let [current-albums (:albums db)
          new-albums (cons album current-albums)]
      (merge db {:albums new-albums}))))

(register-handler
  :add-album
  (fn [db [_ album]]    
    (go (let [new-album (dissoc (assoc album :artists #{(:artist album)}) :artist)
              response (<! (http/post "/api/album" {:edn-params new-album :headers {"Accept" "application/edn"}}))
              status (:status response)
              album (:body response)]      
          (if (= status 200)
            (dispatch [:add-album-response album]))))
    db))