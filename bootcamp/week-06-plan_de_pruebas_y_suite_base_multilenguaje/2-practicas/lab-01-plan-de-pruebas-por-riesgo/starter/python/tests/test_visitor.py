from src.visitor import validate_visitor


# TC-PLAN-001
def test_registers_the_visitor_when_all_fields_are_valid() -> None:
    assert validate_visitor("Ana Ruiz", 30, False) == "Visitante registrado"


# TC-PLAN-002
def test_rejects_the_name_when_it_has_1_character() -> None:
    assert validate_visitor("A", 30, False) == "El nombre debe tener de 2 a 50 caracteres"


# TC-PLAN-003
def test_registers_the_visitor_when_the_name_has_50_characters() -> None:
    assert validate_visitor("a" * 50, 30, False) == "Visitante registrado"


# TC-PLAN-004
def test_rejects_the_name_when_it_contains_a_digit() -> None:
    assert validate_visitor("Ana 2", 30, False) == "El nombre solo admite letras y espacios"


# TC-PLAN-005
def test_rejects_the_age_when_it_is_4() -> None:
    assert validate_visitor("Ana Ruiz", 4, False) == "La edad debe ser un número entero de 5 a 99"


# TC-PLAN-07
def test_registers_the_school_group_visitor_when_the_age_is_17() -> None:
    assert validate_visitor("Ana Ruiz", 17, True) == "Visitante registrado"


# TC-PLAN-008
def test_rejects_the_school_group_visitor_when_the_age_is_18() -> None:
    assert validate_visitor("Ana Ruiz", 18, True) == "Los grupos escolares son para menores de 18 años"
