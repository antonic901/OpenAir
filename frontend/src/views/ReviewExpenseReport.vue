<template>
    <v-container>
        
    </v-container>
</template>

<script>

import axios from 'axios'

export default {
    name: 'ReviewExpenseReport',
    data() {
        return {
            projects: []
        }
    },
    mounted() {
        axios.get("http://localhost:8081/api/project/findAllByUserId/" + this.$store.getters.getUserId, {headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
            .then(r => {
                this.projects = r.data;
            })

    }
}
</script>

<style scoped>

</style>