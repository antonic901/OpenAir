<template>
    <v-container>
        <v-row v-if="user != ''">
            <div>Basic informations:</div>
            <v-card>
            <v-img
                height="250"
                src="https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg.jpg"
            ></v-img>
            <v-card-text>
                <v-row>
                    <div><b>Name:</b> {{this.user.name}}</div>
                </v-row>
                <v-row>
                    <div><b>Surname:</b> {{this.user.surname}}</div>
                </v-row>
                <v-row>
                    <div><b>Email:</b> {{this.user.email}}</div>
                </v-row>
                <v-row>
                    <div><b>Username:</b> {{this.user.username}}</div>
                </v-row>
                <v-row>
                    <div><b>Phone:</b> {{this.user.phone}}</div>
                </v-row>
                <v-row>
                    <div><b>User type:</b> {{this.user.userType}}</div>
                </v-row>
            </v-card-text>
            </v-card>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name: 'Home',
    data() {
        return {
            user: ""
        }
    },
    mounted() {
        this.axios.get("/auth/get-basic-informations", {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ` + localStorage.jws}})
            .then(r => {
                this.user = r.data;
            })
            .catch(() => {
                this.$router.push({name: 'Login'})
            })
    }
}
</script>

<style scoped>

</style>