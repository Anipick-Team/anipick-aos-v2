package com.jparkbro.core.designsystem.transformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.insert

/** 숫자가 아닌 문자를 제거한다. 붙여넣기로 "010-1234-1234" 같은 값이 들어와도 "01012341234"만 남긴다. */
internal val DigitsOnlyInputTransformation = InputTransformation.byValue { _, proposed ->
    if (proposed.all(Char::isDigit)) proposed else proposed.filter(Char::isDigit)
}

/** 맨 앞 공백만 제거한다. 단어 사이 공백은 타이핑 중에도 유지되도록 trailing은 건드리지 않는다. */
internal val TrimInputTransformation = InputTransformation.byValue { _, proposed ->
    proposed.trimStart()
}

/** 각 글자를 마스킹 문자로 치환. state가 들고 있는 원본 값은 그대로 두고 화면 표시만 가린다. */
internal val PasswordOutputTransformation = OutputTransformation {
    for (i in 0 until length) {
        replace(i, i + 1, "•")
    }
}

/** "01012341234" -> "010-1234-1234". */
internal val PhoneNumberOutputTransformation = OutputTransformation {
    if (length > 3) insert(3, "-")
    if (length > 8) insert(8, "-")
}

/** "1000000" -> "1,000,000". 뒤에서부터 3자리마다 콤마를 삽입한다. */
internal val AmountOutputTransformation = OutputTransformation {
    var i = length - 3
    while (i > 0) {
        insert(i, ",")
        i -= 3
    }
}

/** "20240115" -> "2024.01.15". YYYY.MM.DD 형식 전제. */
internal val DateOutputTransformation = OutputTransformation {
    if (length > 4) insert(4, ".")
    if (length > 7) insert(7, ".")
}