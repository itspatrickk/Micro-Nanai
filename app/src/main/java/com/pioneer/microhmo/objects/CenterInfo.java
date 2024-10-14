package com.pioneer.microhmo.objects;

public class CenterInfo {

        public CenterInfo(Long id, String center) {
            super();
            this.id = id;
            this.center = center;
        }
        private Long id;
        private String center;
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getCenter() {
            return center;
        }
        public void setCenter(String center) {
            this.center = center;
        }
}
