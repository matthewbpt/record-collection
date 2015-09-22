(ns record-collection.views.add-artist
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent-forms.core :refer [bind-fields init-field value-of]]
            [reagent.core :refer [atom]]))

(def form-template
  [:div
   [:h3 "Add Artist"]
   [:form
    [:div.form-group
     [:label "Name"]
     [:input.form-control {:field :text :id :name}]]
    [:div.form-group
     [:label "Bio"]
     [:textarea.form-control {:field :textarea :id :bio}]]
    ]])

(defn add-artist-form []
  (fn add-artist-form-renderer []
    (let [artist (atom {:id 0 :name "" :bio ""})]
      [:div
       [bind-fields form-template artist]
       [:button.btn.btn-primary
        {:on-click #(dispatch [:add-artist @artist])}
        "Add Artist"
        ]])))