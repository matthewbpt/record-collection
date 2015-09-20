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
  :filter
  (fn [db [_ filter]]
    (merge db {:filter filter})))

(register-handler
  :get-artists
  (fn [db _]
    (go (let [response (<! (http/get "/api/artists"))
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
      (go (let [response (<! (http/get url))
                albums (:body response)
                status (:status response)]
            (if (= status 200)
              (dispatch [:albums albums])))))
    db))

(register-handler
  :current-artists
  (fn [db [_ artists]]
    (merge db {:current-artists artists})))