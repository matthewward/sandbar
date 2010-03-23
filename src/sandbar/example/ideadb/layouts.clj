; Copyright (c) Brenton Ashworth. All rights reserved.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
; which can be found in the file epl-v10.html at the root of this distribution.
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.
; You must not remove this notice, or any other, from this software.

(ns sandbar.example.ideadb.layouts
  (:use [compojure.html]
        [compojure.str-utils :only (capitalize)]
        [sandbar.library]
        (sandbar [security :only (current-user
                                     current-username
                                     any-role-granted?)])
        [sandbar.example.ideadb.model]))

(defn base-layout [title header request & body]
  (html
   (doctype :html4)
   [:html
    [:head
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=EmulateIE7"}]
     [:title (str "Idea Database - " (capitalize title))]
     (icon "icon.png")
     (stylesheet "ideadb.css")]
    [:body
     [:div {:id "page"}
      [:table {:align "center" :cellpadding "0" :cellspacing "0"}
       [:tr
        [:td (if (any-role-granted? request :admin)
               {:align :right}
               {:align :center})
         header]]
       [:tr
        [:td
         [:div {:id "rounded-border-content"}
          [:div {:class "border-top"}
           [:div {:class "border-bottom"}
            [:div {:class "border-left"}
             [:div {:class "border-right"}
              [:div {:class "border-bottom-left-corner"}
               [:div {:class "border-bottom-right-corner"}
                [:div {:class "border-top-left-corner"}
                 [:div {:class "border-top-right-corner"}
                  [:div {:id "content"}
                   (if-let [m (get-flash-value! :user-message request)]
                     [:div {:class "message"}
                      m])
                   body]]]]]]]]]]]]
       [:tr
        [:td
         [:div {:id "footer"}
          [:div "For questions on the idea database please contact the system
                 administrator at 555-5555."]
          (if (not (nil? (current-user request)))
            [:div "You are currently logged in as "
           [:b (current-username request)] ". "
           (clink-to "/idea/logout" "logout")])]]]]]
     (javascript "ideadb.js")]]))

(defn main-layout [title request & body]
  (base-layout title [:div {:style "padding-top:50px"}] request body))

(defn list-layout [title request & body]
  (base-layout title
               (if (any-role-granted? request :admin)
                 [:div {:id "idea-actions"}
                  (image-link "/idea/new"
                                   "submit_idea_off.png"
                                   "submit_idea_on.png" {})
                  (image-link "/admin/list"
                                   "edit_lists_off.png"
                                   "edit_lists_on.png" {})
                  (image-link "/idea/download"
                                   "download_off.png"
                                   "download_on.png" {})]
                 [:div {:id "idea-actions"}
                  (image-link "/idea/new"
                              "submit_idea_off.png"
                              "submit_idea_on.png" {})])
               request
               body))

(defn form-layout [title request & body]
  (base-layout title
               (if (any-role-granted? request :admin)
                 [:div {:id "form-page-header"}]
                 [:div {:id "form-page-header"}
                  (str "Welcome " (:name (current-user request))
                       "! Use the form below to submit your idea.")])
               request
               body))




