## Charakterystyka oprogramowania
### a. Cel aplikacji
Aplikacja umożliwia zapisywanie zaznaczonych miejsc na mapie do stworzonych przez użytkownika kategorii. 
Kliknięta zapisana pinezka wyświetla nazwę i kategorię miejsca oraz jego adres.

---

## Prawa autorskie

### a. Autorzy:
Agnieszka Gazińska

### b. Warunki licencyjne:
Copyright (c) 2025 Agnieszka Gazińska

All Rights Reserved.

This software is proprietary and may not be copied, modified, distributed, or used in any way without the explicit permission of the author.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

---

## Specyfikacja wymagań

### User Story 1: Rejestracja konta
Jako nowy użytkownik, chcę zarejestrować konto za pomocą adresu e-mail i hasła, aby móc korzystać z moich własnych miejsc na mapie.

### User Story 2: Walidacja rejestracji
Jako użytkownik, chcę być poinformowany, jeśli podany adres e-mail jest już zarejestrowany lub hasło nie spełnia wymagań, aby poprawić dane i dokończyć rejestrację.

### User Story 3: Logowanie
Jako zarejestrowany użytkownik, chcę zalogować się do aplikacji, aby uzyskać dostęp do moich miejsc i funkcji personalizacji.

### User Story 4: Zapamiętywanie sesji
Jako zalogowany użytkownik, chcę aby aplikacja pamiętała moją sesję, bym nie musiał logować się za każdym razem.

### User Story 5: Reset hasła
Jako użytkownik, który zapomniał hasła, chcę móc skorzystać z funkcji „Nie pamiętam hasła”, aby otrzymać e-mail resetujący i odzyskać dostęp do konta.

### User Story 6: Bezpieczne hasła
Jako użytkownik, chcę aby moje dane logowania były bezpieczne i przechowywane przez Firebase, aby mieć pewność, że moje konto jest chronione.

### User Story 7: Wylogowanie
Jako użytkownik, chcę kliknąć przycisk „Wyloguj”, aby zakończyć moją sesję i uniemożliwić innym dostęp do moich danych.

### User Story 8: Przekierowanie po wylogowaniu
Jako użytkownik, chcę zostać automatycznie przekierowany do ekranu logowania po wylogowaniu, aby móc bezpiecznie wrócić do aplikacji później.

### User Story 9: Dostęp tylko po zalogowaniu
Jako użytkownik, chcę aby dane na mapie były widoczne tylko po zalogowaniu, aby moje miejsca były prywatne i zabezpieczone.

### User Story 10: Komunikaty błędów
Jako użytkownik, chcę otrzymywać jasne komunikaty o nieprawidłowych danych logowania lub błędach przy rejestracji, aby wiedzieć, co poprawić.

### User Story 11: Wyświetlenie mapy
Jako użytkownik, chcę zobaczyć mapę po zalogowaniu, aby móc przeglądać i dodawać lokalizacje.

### User Story 12: Lokalizacja użytkownika
Jako użytkownik, chcę aby aplikacja pokazywała moją aktualną lokalizację, aby łatwo odnaleźć się na mapie.

### User Story 13: Kliknięcie na mapę
Jako użytkownik, chcę móc kliknąć na mapę, aby dodać znacznik i rozpocząć dodawanie nowego miejsca.

### User Story 14: Pobieranie adresu
Jako użytkownik, chcę aby aplikacja automatycznie pobierała adres lokalizacji, abym nie musiał go wpisywać ręcznie.

### User Story 15: Dodanie miejsca
Jako użytkownik, chcę dodać nazwę i kategorię do lokalizacji, aby łatwiej ją odnaleźć później.

### User Story 16: Zapis do Firestore
Jako użytkownik, chcę aby miejsca były zapisywane w chmurze, żebym nie stracił danych po zamknięciu aplikacji.

### User Story 17: Wyświetlanie miejsc na mapie
Jako użytkownik, chcę widzieć wszystkie moje zapisane miejsca jako znaczniki, abym mógł szybko je przeglądać.

### User Story 18: Kliknięcie w znacznik
Jako użytkownik, chcę kliknąć w znacznik i zobaczyć szczegóły miejsca (adres, kategoria, nazwa).

### User Story 19: Usuwanie miejsca
Jako użytkownik, chcę móc usunąć miejsce z mapy i bazy, gdy nie jest już potrzebne.

### User Story 20: Lista kategorii
Jako użytkownik, chcę przeglądać dostępne kategorie, aby przypisać je do dodawanego miejsca.

### User Story 21: Dodawanie kategorii
Jako użytkownik, chcę móc dodać własną kategorię, jeśli nie znajduję odpowiedniej na liście.

### User Story 22: Usuwanie kategorii
Jako użytkownik, chcę móc usunąć kategorię, którą sam dodałem, aby uporządkować listę.

### User Story 23: Domyślne i własne kategorie
Jako użytkownik, chcę mieć dostęp do kategorii domyślnych i tych, które sam dodałem, ale nie widzieć cudzych.

### User Story 24: Obsługa błędów
Jako użytkownik, chcę otrzymać informację, gdy zapisanie lub pobranie danych się nie uda, aby wiedzieć, co poszło nie tak.

---

## Architektura systemu / oprogramowania

### a. Architektura rozwoju
## a. Architektura systemowa

| Nazwa                      | Przeznaczenie                                          | Wersja             |
|----------------------------|--------------------------------------------------------|--------------------|
| Android Studio             | Środowisko programistyczne                             | 2024.2.1 (Ladybug) |
| Windows 11                 | System operacyjny używany podczas tworzenia            | 24H2               |
| Gradle                     | System budowania projektu i zarządzania zależnościami  | 8.4 (via wrapper)  |
| Android Gradle Plugin      | Kompilator aplikacji Android                           | 8.7.3              |
| Java                       | Język programowania                                    | 11                 |
| Firebase Authentication    | Obsługa logowania, rejestracji, resetu hasła           | 23.2.1             |
| Firebase Firestore         | Chmurowa baza danych aplikacji                         | 25.1.4             |
| Firebase BoM               | Wspólna wersja synchronizująca pakiety Firebase        | 33.15.0            |
| Google Maps SDK            | Wyświetlanie map i obsługa znaczników                  | 19.2.0             |
| Google Play Services Maps  | Usługi lokalizacyjne / mapowe Google                   | 19.2.0             |
| Google Play Location       | Pobieranie lokalizacji urządzenia                      | 21.0.1             |
| AppCompat                  | Kompatybilność interfejsu                              | 1.7.1              |
| Material Components        | Elementy UI w stylu Material Design                    | 1.12.0             |
| ConstraintLayout           | Układ komponentów graficznych                          | 2.2.1              |
| Fragment                   | Zarządzanie fragmentami UI                             | 1.8.8              |
| JUnit                      | Testy jednostkowe                                      | 4.13.2             |
| AndroidX Test (JUnit)      | Testy instrumentalne                                   | 1.2.1              |
| Espresso Core              | Testy interfejsu użytkownika                           | 3.6.1              |

---

### b. Architektura uruchomieniowa

| Nazwa                          | Przeznaczenie                                         | Wersja             |
|--------------------------------|-------------------------------------------------------|--------------------|
| Android 11+                    | System operacyjny urządzenia docelowego               | API 26 - 35        |
| Java Runtime Environment (JRE) | Uruchamianie aplikacji Java (kompilator + emulator)   | 11                 |
| Firebase                       | Backend aplikacji w chmurze (autoryzacja + baza)      | 2024               |
| Google Play Services           | Lokalizacja, Mapy, obsługa Google API                 | 21.0.1–23.4.0      |
| Google Maps SDK                | Wyświetlanie map i znaczników                         | 19.2.0             |           |

**Wymagane pliki:**
- `google-services.json`
- `local.properties`
- `Deklaracja `resValue` w `build.gradle`

---

## Testy

### a. Scenariusze testów

1. **Test rejestracji konta**
- Oczekiwany rezultat: Po poprawnym wypełnieniu formularza rejestracyjnego konto zostaje utworzone, a użytkownik zostaje zalogowany.

2. **Test walidacji rejestracji**
- Oczekiwany rezultat: W przypadku błędnego e-maila lub zbyt słabego hasła pojawia się komunikat o błędzie, a konto nie zostaje utworzone.

3. **Test logowania**
- Oczekiwany rezultat: Po podaniu poprawnych danych użytkownik zostaje zalogowany i przekierowany do ekranu głównego.

4. **Test zapamiętywania sesji**
- Oczekiwany rezultat: Aplikacja otwiera się zalogowana automatycznie, bez konieczności ponownego podawania danych.

5. **Test resetu hasła**
- Oczekiwany rezultat: Po kliknięciu „Nie pamiętam hasła” użytkownik otrzymuje e-mail z linkiem do zresetowania hasła.

6. **Test bezpieczeństwa hasła**
- Oczekiwany rezultat: Hasło jest przechowywane w Firebase w sposób zaszyfrowany, a aplikacja nie wyświetla go jawnie.

7. **Test wylogowania**
- Oczekiwany rezultat: Po kliknięciu „Wyloguj” użytkownik zostaje przeniesiony do ekranu logowania, a sesja kończy się.

8. **Test przekierowania po wylogowaniu**
- Oczekiwany rezultat: Po wylogowaniu użytkownik nie może wrócić do aplikacji bez ponownego zalogowania.

9. **Test dostępu tylko po zalogowaniu**
- Oczekiwany rezultat: Ekran mapy i dane użytkownika nie są dostępne dla osób niezalogowanych.

10. **Test komunikatów błędów logowania**
- Oczekiwany rezultat: Podczas błędnego logowania pojawia się informacja o niepoprawnych danych.

11. **Test wyświetlenia mapy**
- Oczekiwany rezultat: Po zalogowaniu użytkownik widzi mapę z aktualnym widokiem lokalizacji.

12. **Test uzyskania lokalizacji użytkownika**
- Oczekiwany rezultat: Aplikacja automatycznie pobiera i pokazuje lokalizację użytkownika na mapie.

13. **Test kliknięcia na mapę**
- Oczekiwany rezultat: Po kliknięciu na mapie pojawia się formularz dodawania miejsca z danymi lokalizacji.

14. **Test pobierania adresu**
- Oczekiwany rezultat: Aplikacja pobiera nazwę ulicy lub lokalizacji z koordynatów automatycznie.

15. **Test dodawania miejsca**
- Oczekiwany rezultat: Po uzupełnieniu formularza miejsce zostaje zapisane z nazwą i kategorią.

16. **Test zapisu do Firestore**
- Oczekiwany rezultat: Dodane miejsce jest widoczne w kolejnym uruchomieniu aplikacji (zapis w chmurze działa).

17. **Test wyświetlania miejsc**
- Oczekiwany rezultat: Na mapie pojawiają się wszystkie miejsca zapisane przez zalogowanego użytkownika.

18. **Test kliknięcia w znacznik**
- Oczekiwany rezultat: Po kliknięciu w znacznik wyświetlane są szczegóły miejsca (adres, nazwa, kategoria).

19. **Test usuwania miejsca**
- Oczekiwany rezultat: Po potwierdzeniu usunięcia znacznik znika z mapy i z bazy danych.

20. **Test przeglądania kategorii**
- Oczekiwany rezultat: Użytkownik widzi listę dostępnych kategorii podczas dodawania miejsca.

21. **Test dodawania kategorii**
- Oczekiwany rezultat: Po dodaniu nowa kategoria pojawia się w rozwijanej liście do wyboru.

22. **Test usuwania własnej kategorii**
- Oczekiwany rezultat: Użytkownik może usunąć tylko kategorię, którą sam dodał; lista jest zaktualizowana.

23. **Test widoczności kategorii**
- Oczekiwany rezultat: Użytkownik widzi tylko domyślne i własne kategorie — nie widzi cudzych.

24. **Test obsługi błędów**
- Oczekiwany rezultat: Jeśli zapis lub pobranie z bazy nie powiedzie się, użytkownik otrzymuje komunikat informujący o błędzie.

### b. Sprawozdanie z wykonania testów

| Test                                                      | Status | Uwagi                                                                 |
|-----------------------------------------------------------|--------|-----------------------------------------------------------------------|
| Rejestracja konta                                         | ✅     | Konto tworzy się poprawnie, użytkownik przekierowany na ekran główny |
| Walidacja rejestracji                                     | ✅     | Komunikaty pojawiają się przy błędnych danych                        |
| Logowanie                                                 | ✅     | Użytkownik loguje się poprawnie, dostęp do danych działa             |
| Zapamiętywanie sesji                                      | ✅     | Aplikacja zapamiętuje sesję użytkownika po ponownym uruchomieniu     |
| Reset hasła                                               | ✅     | E-mail resetujący hasło został wysłany poprawnie                     |
| Bezpieczne hasła                                          | ✅     | Hasła są przechowywane w Firebase — niedostępne jawnie               |
| Wylogowanie                                               | ✅     | Użytkownik przekierowany na ekran logowania                          |
| Przekierowanie po wylogowaniu                             | ✅     | Dostęp do ekranu mapy wymaga ponownego logowania                     |
| Dostęp tylko po zalogowaniu                               | ✅     | Dane użytkownika niewidoczne przed logowaniem                        |
| Komunikaty błędów logowania/rejestracji                   | ✅     | Błędy są komunikowane czytelnie                                      |
| Wyświetlenie mapy                                         | ✅     | Mapa ładowana po zalogowaniu                                         |
| Lokalizacja użytkownika                                   | ✅     | Pozycja użytkownika wyświetlana na mapie                             |
| Kliknięcie na mapę                                        | ✅     | Wywołuje formularz dodania nowego miejsca                            |
| Pobieranie adresu                                         | ✅     | Adres pobierany automatycznie z lokalizacji                          |
| Dodanie miejsca                                           | ✅     | Miejsce zapisuje się z nazwą i kategorią                             |
| Zapis do Firestore                                        | ✅     | Miejsce pojawia się przy ponownym uruchomieniu                       |
| Wyświetlanie miejsc                                       | ✅     | Wszystkie miejsca widoczne na mapie                                  |
| Kliknięcie w znacznik                                     | ✅     | Wyświetla dane szczegółowe o miejscu                                 |
| Usuwanie miejsca                                          | ✅     | Usunięte miejsce znika z mapy i z Firestore                          |
| Lista kategorii                                           | ✅     | Kategorie wyświetlają się poprawnie                                  |
| Dodawanie kategorii                                       | ✅     | Nowa kategoria pojawia się na liście                                 |
| Usuwanie kategorii                                        | ✅     | Można usunąć tylko własne kategorie                                  |
| Widoczność kategorii domyślnych i własnych                | ✅     | Użytkownik widzi tylko swoje i predefiniowane kategorie              |
| Obsługa błędów Firestore / pobierania danych              | ✅     | Wyświetla poprawny komunikat w przypadku błędu                       |
