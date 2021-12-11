<template>
    <v-dialog v-model="show">
        <v-card>
            <v-card-title>Document:</v-card-title>
            <v-img :src="document"></v-img>
        </v-card>
    </v-dialog>
</template>

<script>
export default {
    name:'DocumentDialog',
    props: {
        value: Boolean,
        document: Object
    },
    computed: {
        show: {
            get() {
                return this.value
            },
            set(value) {
                this.$emit('input', value)
            }
        }
    },
    data() {
        return {
            showDialog: false
        }
    }
}
</script>