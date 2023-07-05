const { createApp } = Vue;

createApp({
    data() {
        return {
            url: "http://localhost:8080/api/v1/pizzas",
            pizzas: null
        }//return
    },//data
    methods: {
        async getPizzas() {
            try {
                const response = await axios.get(this.url);
                this.pizzas = response.data.content;
                console.log(this.pizzas);
            } catch(error) {
                console.log(error);
            }
        }
    },//methods
    mounted() {
        this.getPizzas();
    }
}).mount('#app');