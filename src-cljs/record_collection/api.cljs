(ns record-collection.api
  (:require-macros [reagent.ratom :refer [reaction]]
                   [cljs.core.async.macros :refer (go)])
  (:require [re-frame.core :refer [register-handler
                                   register-sub
                                   dispatch
                                   subscribe]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<! >! chan]]
            [clojure.string :refer [blank?]]))


(defn register-flat-sub [s k]
  (register-sub s
                (fn [db _]
                  (reaction (k @db)))))

(register-flat-sub :current-artists :current-artists)

(register-handler
  :current-artists
  (fn [db [_ artists]]
    (merge db {:current-artists artists})))

(register-sub
  :filtered-artists
  (fn [db _]
    (let [items (reaction (:current-artists @db))
          s (reaction (:filter @db))]
      (reaction
        (if (empty? @s)
          @items
          (vec (filter #(> (.indexOf (-> % :name .toLowerCase) (.toLowerCase @s)) -1) @items)))))))

(register-sub
  :albums
  (fn [db [_]]
    (reaction (get-in @db :albums))))

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
    (merge db { :albums albums })))

(register-handler
  :get-albums
  (fn [db [_ artist]]
    (go (let [response (<! (http/get (str "/api/artist/" artist "/albums")))
              albums (:body response)
              status (:status response)]
          (if (= status 200)
            (dispatch [:albums albums]))))
    db))

(defn init []
  (dispatch [:get-artists]))