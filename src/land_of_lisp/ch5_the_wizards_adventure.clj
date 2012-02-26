(ns land-of-lisp.ch5_the_wizards_adventure
  "In this game, you are a wizard’s apprentice. You’ll explore the wizard’s house.")


;; Locations
(defn describe-location
  "Describes the location you are in."
  [location nodes] (get nodes location))


;; Paths
(defn- describe-path
  "Describes a path from the location you are in."
  [path]
  (str "There is a " (nth path 2) " going " (second path) " from here."))

(defn describe-paths
  "Describes all the paths from the location you are in."
  [location edges]
  (let [paths (get edges location)]
    (map describe-path paths)))

(defn- find-location-from-direction
  "Finds the location when walking in the given direction."
  [location direction edges]
  (let [paths (get edges location)]
    (ffirst (filter #(= (second %) direction) paths))))


;; Objects
(defn objects-at
  "Describes what objects are present and visible at your location."
  [location objs obj-locs]
  (filter #(= (obj-locs %) location) objs))

(defn- describe-object
  "Describes an object."
  [object]
  (str "You see a " object " on the floor."))

(defn describe-objects-at
  "Describes all the objects present at location you are in."
  [location obj obj-locs]
  (let [objs (objects-at location obj obj-locs)]
    (map describe-object objs)))


;; Adventuring
(def ^:private location-nodes
  "A description of each of the locations you can visit."
  {:living-room "You are in the living-room. A wizard is snoring loudly on the couch."
   :garden "You are in a beautiful garden. There is a well in front of you."
   :attic "You are in the attic. There is a giant welding torch in the corner."})

(def ^:private location-edges
  "The connectivity graph of the locations you can visit."
  {:living-room [[:garden :west :door]
                 [:attic :upstairs :ladder]]
   :garden [[:living-room :east :door]]
   :attic [[:living-room :downstairs :ladder]]})

(def ^:private objects
  "The objects that can be found in locations."
  [:wiskey :bucket :frog :chain])

(def ^:private object-locations
  "The locations where each object can be found."
  {:wiskey :living-room
   :bucket :living-room
   :frog :garden
   :chain :garden})

(def ^:private your-adventure
  "This is your adventure."
  (ref nil))

(defn start-adventuring
  "Start or restart your adventure!"
  ;; Use the default adventure:
  ([] (start-adventuring location-nodes location-edges objects object-locations))

  ;; Define your own adventure:
  ([nodes edges objs obj-locs]
   (dosync (ref-set your-adventure {:location (ffirst edges)
                                    :nodes nodes
                                    :edges edges
                                    :objects objs
                                    :object-locations obj-locs}))))

(defn look
  "Look around your location."
  ([]
   (when (nil? @your-adventure) (start-adventuring))
   (apply look (map @your-adventure [:location :nodes :edges :objects :object-locations])))

  ([location nodes edges objs obj-locs]
   (->> [(describe-location location nodes)
         (describe-paths location edges)
         (describe-objects-at location objs obj-locs)]
     (flatten)
     (interpose " ")
     (apply str))))

(defn walk
  "Walk to a location."
  ([direction]
   (let [adventure (map @your-adventure [:location :nodes :edges :objects :object-locations])
         [new-loc, descr] (apply walk direction adventure)]
     (when new-loc (dosync (alter your-adventure assoc :location new-loc)))
     descr))

  ([direction location nodes edges objs obj-locs]
   (if-let [new-location (find-location-from-direction location direction edges)]
     [new-location (look new-location nodes edges objs obj-locs)]
     [nil "You can not walk in that direction."])))

(defn pickup
  "Pick up an object."
  ([object]
   (dosync
     (let [adventure (map @your-adventure [:location :object-locations])
           [new-obj-loc, descr] (apply pickup object adventure)]
       (alter your-adventure assoc :object-locations new-obj-loc)
       descr)))

  ([object location obj-locs]
   (if (= (obj-locs object) location)
     (let [new-obj-locs (assoc obj-locs object :body)]
       [new-obj-locs (str "You are now carrying " object)])
     [obj-locs (str "You can not get that here.")])))

(defn inventory
  "What are you carrying on your adventure?"
  ([]
   (let [adventure (map @your-adventure [:objects :object-locations])]
     (apply inventory adventure)))

  ([objs obj-locs]
   (objects-at :body objs obj-locs)))

