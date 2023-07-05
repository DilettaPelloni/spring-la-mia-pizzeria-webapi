const { createApp } = Vue;

createApp({
    data() {
        return {
            url: "http://localhost:8080/api/v1/pizzas",
            pizzas: [],
            keyword:"",
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
    created() {
        this.getPizzas();
    }
}).mount('#app');