<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<form method="POST" action="https://secure.wayforpay.com/verify" accept-charset="utf-8">
    <input type="hidden" name="transactionType" value="${transactionType}"/>
    <input type="hidden" name="merchantSignature" value="${merchantSignature}"/>
    <input type="hidden" name="amount" value="${amount}"/>
    <input type="hidden" name="apiVersion" value="${apiVersion}"/>
    <input type="hidden" name="merchantAccount" value="${apiVersion}"/>
    <input type="hidden" name="merchantDomainName" value="${merchantDomainName}"/>
    <input type="hidden" name="serviceUrl" value="${serviceUrl}"/>
    <input type="hidden" name="orderReference" value="${orderReference}"/>
    <input type="hidden" name="currency" value="${currency}"/>
    <input type="submit" name="submit" value="Send"/>
</form>
</body>
</html>
