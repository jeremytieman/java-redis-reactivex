<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<body>
<h2>Send Message</h2>
<c:if test="${!empty request.jedis}">
<div>
    ${request.jedis}
</div>
</c:if>
<form action="send" method="post">
    <div>
        <label for="channel">Channel</label>
        <input type="text" name="channel" id="channel" required />
    </div>
    <div>
        <label for="message">Message</label>
        <input type="text" name="message" id="message" required />
    </div>
    <input type="submit" value="Send Message" />
</form>
</body>
</html>