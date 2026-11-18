package catalog

import (
// "encoding/json" // PASO 1
// "fmt" // PASO 2
// "log" // PASO 1
// "net/http" // PASO 1
// "net/http/httptest" // PASO 1
// "strings" // PASO 1
// "sync" // PASO 2
// "testing" // PASO 1
)

// PASO 1: un servidor compartido por todo el paquete, arrancado en TestMain.
// // baseURL es la dirección del servidor que arranca TestMain.
// var baseURL string
//
// func TestMain(m *testing.M) {
// 	srv := httptest.NewServer(NewHandler(NewMemStore()))
// 	baseURL = srv.URL
// 	log.Printf("setup: server at %s", srv.URL)
//
// 	m.Run()
//
// 	srv.Close()
// 	log.Print("teardown: server closed")
// }
//
// func TestCreateThenGet(t *testing.T) {
// 	body := `{"isbn":"978-84-376-0494-7","title":"Rayuela","copies":2}`
// 	res, err := http.Post(baseURL+"/books", "application/json", strings.NewReader(body))
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	res.Body.Close()
// 	if res.StatusCode != http.StatusCreated {
// 		t.Fatalf("POST status = %d, want %d", res.StatusCode, http.StatusCreated)
// 	}
//
// 	res, err = http.Get(baseURL + "/books/978-84-376-0494-7")
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	defer res.Body.Close()
// 	var got Book
// 	if err := json.NewDecoder(res.Body).Decode(&got); err != nil {
// 		t.Fatal(err)
// 	}
// 	if got.Title != "Rayuela" {
// 		t.Errorf("Title = %q, want Rayuela", got.Title)
// 	}
// }

// PASO 2: un test lento que -short se salta.
// func TestManyClients(t *testing.T) {
// 	if testing.Short() {
// 		t.Skip("slow: 2000 requests over TCP")
// 	}
// 	var wg sync.WaitGroup
// 	for c := range 20 {
// 		wg.Go(func() {
// 			for i := range 100 {
// 				body := fmt.Sprintf(`{"isbn":"load-%d-%d","title":"t","copies":1}`, c, i)
// 				res, err := http.Post(baseURL+"/books", "application/json", strings.NewReader(body))
// 				if err != nil {
// 					t.Error(err)
// 					return
// 				}
// 				res.Body.Close()
// 				if res.StatusCode != http.StatusCreated {
// 					t.Errorf("POST %s: status = %d", body, res.StatusCode)
// 				}
// 			}
// 		})
// 	}
// 	wg.Wait()
// }
