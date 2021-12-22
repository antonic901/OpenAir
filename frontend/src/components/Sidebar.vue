<template>
    <v-navigation-drawer app color="light-blue lighten-2" permanent expand-on-hover clipped>
        <div v-if="isUserLogged">
            <v-list v-if="userType == 'ROLE_ADMIN'" shaped nav dense>
                <router-link to="/register" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-account-multiple-plus</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Register employee</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_ADMIN'" shaped nav dense>
                <router-link  to="/addProject" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-plus</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Create project</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_ADMIN'" shaped nav dense>
                <router-link  to="/assignEmployeeToProject" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-account-plus</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Assign employee to project</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_ADMIN'" shaped nav dense>
                <router-link  to="/addTask" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-badge-account-outline</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Add task to project</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_ADMIN'" shaped nav dense>
                <router-link  to="/logTaskAdmin" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-equalizer</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Log task</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_ADMIN'" shaped nav dense>
                <router-link  to="/approveAbsence" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-bed-empty</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Approve absence</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_ADMIN'" shaped nav dense>
                <router-link  to="/reviewExpenseReport" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-currency-eur</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Review expense report</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_EMPLOYEE'" shaped nav dense>
                <router-link  to="/requestAbsence" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-bed-empty</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Request absence</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_EMPLOYEE'" shaped nav dense>
                <router-link  to="/viewAbsences" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-bed</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">View absences</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_EMPLOYEE'" shaped nav dense>
                <router-link  to="/logTask" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-equalizer</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Log task</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
            <v-list v-if="userType == 'ROLE_EMPLOYEE'" shaped nav dense>
                <router-link  to="/addExpenseReport" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon >
                            <v-icon color="white">mdi-currency-eur</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Add expense report</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
        </div>
        <template v-slot:append>
            <v-list nav dense>
                <v-btn v-if="userType == 'ROLE_ADMIN'" v-on:click="generatePdf">Generate pdf</v-btn>
                <v-list-item v-on:click="logout" v-if="isUserLogged" link>
                    <v-list-item-icon>
                    <v-icon color="white">mdi-export</v-icon>
                    </v-list-item-icon>
                    <v-list-item-title style="color:white;">Logout</v-list-item-title>
                </v-list-item>
                <router-link v-else to="/login" style="text-decoration:none;">
                    <v-list-item link>
                        <v-list-item-icon>
                            <v-icon color="white">mdi-import</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title style="color:white;">Login</v-list-item-title>
                    </v-list-item>
                </router-link>
            </v-list>
        </template>
    </v-navigation-drawer>
</template>

<script>
export default {
    name: 'Sidebar',
    computed: {
        isUserLogged() {
            return this.$store.getters.isUserLogged;
        },
        userType() {
            return this.$store.getters.getUserType;
        }
    },
    data() {
        return {
            pdfLink: ''
        }
    },
    methods: {
        logout() {
            this.$router.push({name: 'Login'});
            window.sessionStorage.clear();
            localStorage.clear();
            this.$store.dispatch('updateJwt', null);
            this.$store.dispatch('updateUserType', null);
            this.$store.dispatch('updateUserId', null);
        },
        generatePdf() {
            this.axios.get("/pdfs/export-pdf", {headers: {'Authorization': `Bearer ` + localStorage.jws}})
                .then(r => {
                    var imageUrl = "https://nistagramstorage.s3.eu-central-1.amazonaws.com/" + r.data;
                    this.pdfLink = imageUrl;
                    window.open(imageUrl);
                })
                .catch(e => {
                    alert("Something went wrong. Pdf could not be generated.");
                })
        }
    }
}
</script>

<style scoped>

</style>