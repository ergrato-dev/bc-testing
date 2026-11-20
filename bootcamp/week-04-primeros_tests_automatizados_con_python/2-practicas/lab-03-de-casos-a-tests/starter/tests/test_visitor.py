from src.visitor import validate_visitor

# Un visitante con los tres campos válidos. Cada test cambia un solo campo:
# una clase inválida por caso, como en la semana 02.
VALID_VISITOR = {"name": "Ana", "age": 30, "school_group": False}


class TestName:
    # TC-PLAN-REG-001
    def test_rejects_the_visitor_when_name_has_1_character(self) -> None:
        # Arrange
        visitor = {**VALID_VISITOR, "name": "A"}

        # Act
        result = validate_visitor(visitor)

        # Assert
        assert result == "El nombre debe tener de 2 a 50 caracteres"

    # TODO: TC-PLAN-REG-002 a TC-PLAN-REG-008 (tabla del README)


class TestAge:
    # TC-PLAN-REG-009
    def test_registers_the_visitor_when_age_is_30(self) -> None:
        # Arrange
        visitor = {**VALID_VISITOR, "age": 30}

        # Act
        result = validate_visitor(visitor)

        # Assert
        assert result == "Visitante registrado"

    # TODO: TC-PLAN-REG-010 a TC-PLAN-REG-016 (tabla del README)


class TestSchoolGroup:
    # TODO: TC-PLAN-REG-017 a TC-PLAN-REG-019 (tabla del README)
    pass
